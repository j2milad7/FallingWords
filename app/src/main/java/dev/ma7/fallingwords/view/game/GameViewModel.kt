package dev.ma7.fallingwords.view.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ma7.fallingwords.domain.model.Question
import dev.ma7.fallingwords.domain.usecase.QuestionsUseCase
import dev.ma7.fallingwords.view.base.BaseViewModel
import dev.ma7.fallingwords.view.base.ViewState
import dev.ma7.fallingwords.view.util.SingleEventLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val questionsUseCase: QuestionsUseCase
) : BaseViewModel() {

    private val args: GameFragmentArgs by lazy(LazyThreadSafetyMode.NONE) {
        GameFragmentArgs.fromSavedStateHandle(savedStateHandle)
    }

    private var score = DEFAULT_SCORE
        set(value) {
            field = value
            _scoreStateFlow.value = value
        }

    private val _toastMessageSharedFlow by lazy(LazyThreadSafetyMode.NONE) {
        MutableSharedFlow<String>()
    }
    val toastMessageSharedFlow: SharedFlow<String> by lazy(LazyThreadSafetyMode.NONE) {
        _toastMessageSharedFlow.asSharedFlow()
    }

    private val _timerStateFlow = MutableStateFlow(DEFAULT_TIME)
    val timerStateFlow: StateFlow<Int> = _timerStateFlow.asStateFlow()

    private val _scoreStateFlow = MutableStateFlow(score)
    val scoreStateFlow: StateFlow<Int> = _scoreStateFlow.asStateFlow()

    private val _wordStateFlow = MutableStateFlow(value = "")
    val wordStateFlow: StateFlow<String> = _wordStateFlow.asStateFlow()

    private val _translationStateFlow = MutableStateFlow(value = "")
    val translationStateFlow: StateFlow<String> = _translationStateFlow.asStateFlow()

    private val _viewStateLiveData = SingleEventLiveData<ViewState>()
    val viewStateLiveData: LiveData<ViewState> = _viewStateLiveData

    private var questionList: List<Question> = emptyList()
    private var timerJob: Job? = null
    private var currentQuestionIndex = DEFAULT_QUESTION_INDEX

    init {
        getData()
    }

    fun onStartGame() {
        resetGame()
        onNextQuestion()
        initCountDownTimer(totalSeconds = args.gameDifficulty.wordsCount * TIMER_FACTOR)
    }

    fun onCorrectAnswerClick() {
        checkAnswer(isCorrect = true)
    }

    fun onWrongAnswerClick() {
        checkAnswer(isCorrect = false)
    }

    fun onNextQuestion() {
        if (questionList.isEmpty()) return
        if (currentQuestionIndex >= questionList.size - 1) {
            finishGame(isTimeUp = false)
            return
        }
        currentQuestionIndex++
        val question: Question = questionList[currentQuestionIndex]
        _wordStateFlow.value = question.english
        _translationStateFlow.value = question.spanish
        _viewStateLiveData.value = ViewState.NextQuestion
    }

    private fun resetGame() {
        currentQuestionIndex = DEFAULT_QUESTION_INDEX
        score = DEFAULT_SCORE
        _timerStateFlow.value = DEFAULT_TIME
        _wordStateFlow.value = ""
        _translationStateFlow.value = ""
        timerJob?.cancel()
    }

    private fun checkAnswer(isCorrect: Boolean) {
        if (questionList.isEmpty()) return
        val question: Question = questionList[currentQuestionIndex]
        val isAnswerCorrect = question.isCorrect == isCorrect
        if (isAnswerCorrect) {
            score++
        }
        _viewStateLiveData.value = ViewState.Answer(isAnswerCorrect)
    }

    private fun finishGame(isTimeUp: Boolean) {
        _wordStateFlow.value = ""
        _translationStateFlow.value = ""
        _viewStateLiveData.value = ViewState.Finish(score, isTimeUp)
    }

    private fun getData() {
        _viewStateLiveData.value = ViewState.Loading
        viewModelScope.launch {
            questionsUseCase(args.gameDifficulty).fold(
                ifRight = ::onSuccessResponse,
                ifLeft = ::onErrorResponse,
            )
        }
    }

    private fun onSuccessResponse(questionList: List<Question>) {
        this.questionList = questionList
        _viewStateLiveData.value = ViewState.Loaded
    }

    private fun onErrorResponse(throwable: Throwable) {
        _viewStateLiveData.value = ViewState.Error(throwable)
        viewModelScope.launch { throwable.message?.let { _toastMessageSharedFlow.emit(it) } }
    }

    private fun initCountDownTimer(totalSeconds: Int) {
        timerJob = viewModelScope.launch {
            (totalSeconds - 1 downTo 0).asFlow()
                .onEach { delay(TIMER_DELAY_IN_MILLIS) }
                .onStart { emit(totalSeconds) }
                .conflate()
                .flowOn(Dispatchers.Default)
                .collect {
                    _timerStateFlow.value = it
                    if (it == 0) {
                        finishGame(isTimeUp = true)
                    }
                }
        }
    }

    private companion object {

        const val TIMER_DELAY_IN_MILLIS = 1000L
        const val TIMER_FACTOR = 10
        const val DEFAULT_SCORE = 0
        const val DEFAULT_TIME = 0
        const val DEFAULT_QUESTION_INDEX = -1
    }
}