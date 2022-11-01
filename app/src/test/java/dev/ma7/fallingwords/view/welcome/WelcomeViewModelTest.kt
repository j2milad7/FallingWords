package dev.ma7.fallingwords.view.welcome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.ma7.fallingwords.domain.model.GameDifficulty.EASY
import dev.ma7.fallingwords.domain.model.GameDifficulty.HARD
import dev.ma7.fallingwords.domain.model.GameDifficulty.MEDIUM
import dev.ma7.fallingwords.view.util.Navigation
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WelcomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: WelcomeViewModel

    @Before
    fun setUp() {
        viewModel = WelcomeViewModel()
    }

    @Test
    fun `navigation live data should be called with EASY difficulty argument`() {
        // GIVEN
        val expectationNavDirections = WelcomeFragmentDirections.openGameFragment(EASY)

        // WHEN
        viewModel.onEasyClick()

        // THEN
        val result = (viewModel.navigationLiveData.value as Navigation.Direction).directions
        assertThat(result, `is`(expectationNavDirections))
    }

    @Test
    fun `navigation live data should be called with MEDIUM difficulty argument`() {
        // GIVEN
        val expectationNavDirections = WelcomeFragmentDirections.openGameFragment(MEDIUM)

        // WHEN
        viewModel.onMediumClick()

        // THEN
        val result = (viewModel.navigationLiveData.value as Navigation.Direction).directions
        assertThat(result, `is`(expectationNavDirections))
    }

    @Test
    fun `navigation live data should be called with HARD difficulty argument`() {
        // GIVEN
        val expectationNavDirections = WelcomeFragmentDirections.openGameFragment(HARD)

        // WHEN
        viewModel.onHardClick()

        // THEN
        val result = (viewModel.navigationLiveData.value as Navigation.Direction).directions
        assertThat(result, `is`(expectationNavDirections))
    }
}