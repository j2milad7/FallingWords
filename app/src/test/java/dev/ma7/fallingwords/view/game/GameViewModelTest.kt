package dev.ma7.fallingwords.view.game

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import arrow.core.Either
import dev.ma7.fallingwords.domain.model.GameDifficulty
import dev.ma7.fallingwords.domain.model.GameDifficulty.EASY
import dev.ma7.fallingwords.domain.model.Question
import dev.ma7.fallingwords.domain.usecase.QuestionsUseCase
import dev.ma7.fallingwords.util.CoroutinesTestRule
import dev.ma7.fallingwords.util.questionList
import dev.ma7.fallingwords.view.base.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GameViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val savedStateHandle: SavedStateHandle = mock()
    private val questionsUseCase: QuestionsUseCase = mock()
    private val gameDifficulty = EASY
    private lateinit var viewModel: GameViewModel

    @Before
    fun setUp() = runTest {
        whenever(savedStateHandle.contains(anyString())) doReturn true
        whenever(savedStateHandle.get<GameDifficulty>(anyString())) doReturn gameDifficulty
        whenever(questionsUseCase(any())) doReturn Either.Right(questionList)
        viewModel = GameViewModel(savedStateHandle, questionsUseCase)
    }

    @Test
    fun `successful response should be get once view model is created`() = runTest {
        // THEN
        assertThat(viewModel.viewStateLiveData.value, `is`(ViewState.Loaded))
    }

    @Test
    fun `error response should be get once view model is created`() = runTest {
        // GIVEN
        val exception = Exception("Test")
        whenever(questionsUseCase(any())) doReturn Either.Left(exception)
        viewModel = GameViewModel(savedStateHandle, questionsUseCase)

        // THEN
        launch {
            assertThat(viewModel.viewStateLiveData.value, `is`(ViewState.Error(exception)))
            assertThat(viewModel.toastMessageSharedFlow.first(), `is`(exception.message))
        }
    }

    @Test
    fun `when the question list is empty should be returned from 'onStartGame'`() = runTest {
        // GIVEN
        whenever(questionsUseCase(any())) doReturn Either.Right(emptyList())
        viewModel = GameViewModel(savedStateHandle, questionsUseCase)

        // WHEN
        viewModel.onStartGame()

        // THEN
        assertThat(viewModel.timerStateFlow.value, `is`(GameViewModel.DEFAULT_TIME))
        assertThat(viewModel.scoreStateFlow.value, `is`(GameViewModel.DEFAULT_SCORE))
        assertThat(viewModel.wordStateFlow.value, `is`(""))
        assertThat(viewModel.translationStateFlow.value, `is`(""))
    }

    @Test
    fun `the first question should be set when 'onStartGame' is called`() = runTest {
        // GIVEN
        val question: Question = questionList[0]

        // WHEN
        viewModel.onStartGame()

        // THEN
        assertThat(viewModel.timerStateFlow.value, `is`(GameViewModel.DEFAULT_TIME))
        assertThat(viewModel.scoreStateFlow.value, `is`(GameViewModel.DEFAULT_SCORE))
        assertThat(viewModel.wordStateFlow.value, `is`(question.english))
        assertThat(viewModel.translationStateFlow.value, `is`(question.spanish))
        assertThat(viewModel.viewStateLiveData.value, `is`(ViewState.NextQuestion))
    }

    @Test
    fun `the timer should be started when 'onStartGame' is called`() = runTest {
        // GIVEN
        launch(Dispatchers.Unconfined) {
            val totalSeconds = (gameDifficulty.wordsCount * GameViewModel.TIMER_FACTOR) - 1
            // WHEN
            viewModel.onStartGame()

            // THEN
            delay(1100)
            assertThat(viewModel.timerStateFlow.value, `is`(totalSeconds))
        }
    }

    @Test
    fun `the second question should be set when 'onNextQuestion' is called for the second time`() =
        runTest {
            // GIVEN
            val question: Question = questionList[1]

            // WHEN
            repeat(times = 2) {
                viewModel.onNextQuestion()
            }

            // THEN
            assertThat(viewModel.wordStateFlow.value, `is`(question.english))
            assertThat(viewModel.translationStateFlow.value, `is`(question.spanish))
            assertThat(viewModel.viewStateLiveData.value, `is`(ViewState.NextQuestion))
        }

    @Test
    fun `the game should be finished when 'onNextQuestion' is called for the third time`() =
        runTest {
            // WHEN
            repeat(times = 3) {
                viewModel.onNextQuestion()
            }

            // THEN
            assertThat(viewModel.wordStateFlow.value, `is`(""))
            assertThat(viewModel.translationStateFlow.value, `is`(""))
            assertThat(
                viewModel.viewStateLiveData.value,
                `is`(ViewState.Finish(viewModel.scoreStateFlow.value, isTimeUp = false))
            )
        }

    @Test
    fun `while the question list is empty should be returned from 'onCorrectAnswerClick'`() =
        runTest {
            // GIVEN
            whenever(questionsUseCase(any())) doReturn Either.Right(emptyList())
            viewModel = GameViewModel(savedStateHandle, questionsUseCase)

            // WHEN
            viewModel.onCorrectAnswerClick()

            // THEN
            assertThat(
                viewModel.viewStateLiveData.value,
                not(ViewState.Answer(isCorrect = true))
            )
        }

    @Test
    fun `the Answer state with true value should be set when 'onCorrectAnswerClick'is called`() =
        runTest {
            // GIVEN
            val score = viewModel.scoreStateFlow.value + 1
            viewModel.onNextQuestion()

            // WHEN
            viewModel.onCorrectAnswerClick()

            // THEN
            assertThat(viewModel.viewStateLiveData.value, `is`(ViewState.Answer(isCorrect = true)))
            assertThat(viewModel.scoreStateFlow.value, `is`(score))
        }

    @Test
    fun `the Answer state with true value should be set when 'onWrongAnswerClick'is called`() =
        runTest {
            // GIVEN
            val score = viewModel.scoreStateFlow.value
            viewModel.onNextQuestion()

            // WHEN
            viewModel.onWrongAnswerClick()

            // THEN
            assertThat(viewModel.viewStateLiveData.value, `is`(ViewState.Answer(isCorrect = false)))
            assertThat(viewModel.scoreStateFlow.value, `is`(score))
        }
}