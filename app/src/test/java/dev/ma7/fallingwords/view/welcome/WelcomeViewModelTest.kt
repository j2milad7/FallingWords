package dev.ma7.fallingwords.view.welcome

import dev.ma7.fallingwords.domain.model.GameDifficulty.EASY
import dev.ma7.fallingwords.domain.model.GameDifficulty.HARD
import dev.ma7.fallingwords.domain.model.GameDifficulty.MEDIUM
import dev.ma7.fallingwords.util.CoroutinesTestRule
import dev.ma7.fallingwords.view.util.Navigation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WelcomeViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    lateinit var viewModel: WelcomeViewModel

    @Before
    fun setUp() {
        viewModel = WelcomeViewModel()
    }

    @Test
    fun `navigation live data should be called with EASY difficulty argument`() = runTest {
        // GIVEN
        val expectationNavDirections = WelcomeFragmentDirections.openGameFragment(EASY)

        // WHEN
        viewModel.onEasyClick()

        // THEN
        val result =
            (viewModel.navigationSharedFlow.first() as Navigation.Direction).directions
        assertThat(result, `is`(expectationNavDirections))
    }

    @Test
    fun `navigation live data should be called with MEDIUM difficulty argument`() = runTest {
        // GIVEN
        val expectationNavDirections = WelcomeFragmentDirections.openGameFragment(MEDIUM)

        // WHEN
        viewModel.onMediumClick()

        // THEN
        val result = (viewModel.navigationSharedFlow.first() as Navigation.Direction).directions
        assertThat(result, `is`(expectationNavDirections))
    }

    @Test
    fun `navigation live data should be called with HARD difficulty argument`() = runTest {
        // GIVEN
        val expectationNavDirections = WelcomeFragmentDirections.openGameFragment(HARD)

        // WHEN
        viewModel.onHardClick()

        // THEN
        val result = (viewModel.navigationSharedFlow.first() as Navigation.Direction).directions
        assertThat(result, `is`(expectationNavDirections))
    }
}