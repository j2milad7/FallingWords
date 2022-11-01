package dev.ma7.fallingwords.view.welcome

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ma7.fallingwords.domain.model.GameDifficulty
import dev.ma7.fallingwords.domain.model.GameDifficulty.EASY
import dev.ma7.fallingwords.domain.model.GameDifficulty.HARD
import dev.ma7.fallingwords.domain.model.GameDifficulty.MEDIUM
import dev.ma7.fallingwords.view.base.BaseViewModel
import dev.ma7.fallingwords.view.util.Navigation
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : BaseViewModel() {

    fun onEasyClick() {
        openGameFragment(EASY)
    }

    fun onMediumClick() {
        openGameFragment(MEDIUM)
    }

    fun onHardClick() {
        openGameFragment(HARD)
    }

    private fun openGameFragment(gameDifficulty: GameDifficulty) {
        navigate(
            Navigation.Direction(
                WelcomeFragmentDirections.openGameFragment(gameDifficulty)
            )
        )
    }
}