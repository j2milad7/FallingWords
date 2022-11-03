package dev.ma7.fallingwords.view.game

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.ma7.fallingwords.R
import dev.ma7.fallingwords.databinding.FragmentGameBinding
import dev.ma7.fallingwords.view.base.BaseFragment
import dev.ma7.fallingwords.view.base.ViewState
import dev.ma7.fallingwords.view.util.Navigation
import dev.ma7.fallingwords.view.util.gone
import dev.ma7.fallingwords.view.util.visible
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameFragment : BaseFragment<GameViewModel>() {

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = requireNotNull(_binding)

    override val viewModel: GameViewModel by viewModels()
    private var isTranslationAnimationInitialized: Boolean = false
    private var isResultAnimationInitialized: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
        observeLiveData()
        setListeners()
    }

    private fun collectFlows() {
        with(binding) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch { viewModel.toastMessageSharedFlow.collectLatest(::showToast) }
                    launch { viewModel.wordStateFlow.collectLatest { textGameWord.text = it } }
                    launch {
                        viewModel.translationStateFlow.collectLatest {
                            textGameTranslation.text = it
                        }
                    }
                    launch {
                        viewModel.timerStateFlow.collectLatest {
                            textGameRemainingTime.text = it.toString()
                        }
                    }
                    launch {
                        viewModel.scoreStateFlow.collectLatest {
                            textGameScore.text = it.toString()
                        }
                    }
                }
            }
        }
    }

    private fun observeLiveData() {
        viewModel.viewStateLiveData.observe(viewLifecycleOwner, ::handleViewState)
    }

    private fun setListeners() {
        with(binding) {
            imageGameCorrect.setOnClickListener { viewModel.onCorrectAnswerClick() }
            imageGameWrong.setOnClickListener { viewModel.onWrongAnswerClick() }
        }
    }

    private fun fadeInTranslation() {
        isTranslationAnimationInitialized = true
        with(binding.textGameTranslation) {
            alpha = FADE_OUT_ALPHA
            animate()
                .alpha(FADE_IN_ALPHA)
                .setDuration(FADE_DURATION_IN_MILLIS)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        animateTranslation()
                    }
                })
        }
    }

    private fun fadeOutTranslation() {
        binding.textGameTranslation
            .animate()
            .alpha(FADE_OUT_ALPHA)
            .setDuration(FADE_DURATION_IN_MILLIS)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    showAnswerResult(isCorrect = false)
                }
            })
    }

    private fun animateTranslation() {
        with(binding) {
            val transitionValue = imageGameShark.top - textGameTranslation.top
            textGameTranslation.translationY = DEFAULT_TRANSLATION_Y
            textGameTranslation
                .animate()
                .translationY(transitionValue.toFloat())
                .setDuration(TRANSITION_DURATION_IN_MILLIS)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        fadeOutTranslation()
                    }
                })
        }
    }

    private fun showAnswerResult(isCorrect: Boolean) {
        with(binding) {
            textGameTranslation.animate().cancel()
            imageGameCorrect.isEnabled = false
            imageGameWrong.isEnabled = false
            val resultImageResId = if (isCorrect) {
                R.drawable.background_thumb_up
            } else {
                R.drawable.background_thumb_down
            }
            imageGameResult.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), resultImageResId)
            )
            scaleInResult()
        }
    }

    private fun scaleInResult() {
        isResultAnimationInitialized = true
        with(binding.imageGameResult) {
            scaleX = SCALE_OUT
            scaleY = SCALE_OUT
            animate()
                .scaleX(SCALE_IN)
                .scaleY(SCALE_IN)
                .setDuration(SCALE_DURATION_IN_MILLIS)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        scaleOutResult()
                    }
                })
        }
    }

    private fun scaleOutResult() {
        binding.imageGameResult
            .animate()
            .setStartDelay(SCALE_DELAY_IN_MILLIS)
            .scaleX(SCALE_OUT)
            .scaleY(SCALE_OUT)
            .setDuration(SCALE_DURATION_IN_MILLIS)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    showNextQuestion()
                }
            })
    }

    private fun showNextQuestion() {
        resetViews()
        viewModel.onNextQuestion()
    }

    private fun resetViews() {
        with(binding) {
            textGameTranslation.animate().cancel()
            textGameTranslation.alpha = FADE_OUT_ALPHA
            textGameTranslation.translationY = DEFAULT_TRANSLATION_Y

            imageGameResult.animate().cancel()
            imageGameResult.scaleX = SCALE_OUT
            imageGameResult.scaleY = SCALE_OUT

            imageGameCorrect.isEnabled = true
            imageGameWrong.isEnabled = true
        }
    }

    private fun finishGame(score: Int, isTimeUp: Boolean) {
        resetViews()
        showGameFinalResultDialog(score, isTimeUp)
    }

    private fun showToast(message: String?) {
        if (message.isNullOrEmpty()) return
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun handleViewState(viewState: ViewState) {
        with(binding) {
            when (viewState) {
                ViewState.Loading -> {
                    progressBarGame.visible()
                }
                ViewState.Loaded -> {
                    progressBarGame.gone()
                    showStartGameDialog()
                }
                ViewState.NextQuestion -> {
                    fadeInTranslation()
                }
                is ViewState.Answer -> {
                    showAnswerResult(viewState.isCorrect)
                }
                is ViewState.Finish -> {
                    finishGame(viewState.score, viewState.isTimeUp)
                }
                is ViewState.Error -> {
                    progressBarGame.gone()
                    showToast(viewState.error.message)
                }
            }
        }
    }

    private fun showStartGameDialog() {
        AlertDialog.Builder(requireContext(), R.style.FallingWords_AlertDialog)
            .setTitle(R.string.text_start_title)
            .setMessage(R.string.text_start_message)
            .setCancelable(false)
            .setPositiveButton(R.string.action_start) { dialog, _ ->
                dialog.dismiss()
                viewModel.onStartGame()
            }
            .setNegativeButton(R.string.action_back) { dialog, _ ->
                dialog.dismiss()
                navigate(Navigation.Pop)
            }
            .create()
            .show()
    }

    private fun showGameFinalResultDialog(score: Int, isTimeUp: Boolean) {
        val title = when {
            isTimeUp -> R.string.text_time_up_title
            score > 0 -> R.string.text_result_title
            else -> R.string.text_oh_no_title
        }
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(getString(R.string.text_result_message, score))
            .setCancelable(false)
            .setNeutralButton(R.string.action_ok) { dialog, _ ->
                dialog.dismiss()
                navigate(Navigation.Pop)
            }
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isTranslationAnimationInitialized) {
            binding.textGameTranslation.animate().setListener(null)
        }
        if (isResultAnimationInitialized) {
            binding.imageGameResult.animate().setListener(null)
        }
        _binding = null
    }

    private companion object {

        const val TRANSITION_DURATION_IN_MILLIS = 9000L
        const val FADE_DURATION_IN_MILLIS = 200L
        const val FADE_IN_ALPHA = 1f
        const val FADE_OUT_ALPHA = 0f
        const val SCALE_DURATION_IN_MILLIS = 300L
        const val SCALE_DELAY_IN_MILLIS = 300L
        const val SCALE_IN = 1.5f
        const val SCALE_OUT = 0f
        const val DEFAULT_TRANSLATION_Y = 0f
    }
}