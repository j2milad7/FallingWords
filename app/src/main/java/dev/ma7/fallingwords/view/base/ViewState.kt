package dev.ma7.fallingwords.view.base

sealed class ViewState {

    object Loading : ViewState()

    object Loaded : ViewState()

    object NextQuestion : ViewState()

    data class Answer(val isCorrect: Boolean) : ViewState()

    data class Finish(val score: Int, val isTimeUp: Boolean) : ViewState()

    data class Error(val error: Throwable) : ViewState()
}