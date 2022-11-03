package dev.ma7.fallingwords.view.welcome

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.ma7.fallingwords.R
import dev.ma7.fallingwords.domain.model.GameDifficulty
import dev.ma7.fallingwords.view.MainActivity
import dev.ma7.fallingwords.view.game.GameFragmentArgs
import org.hamcrest.CoreMatchers.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WelcomeFragmentTest {

    @get : Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun check_GameFragment_is_opened_with_EASY_argument_correctly() {
        onView(withId(R.id.button_welcome_easy)).perform(click())
        activityScenarioRule.scenario.onActivity {
            val bundle = it.supportFragmentManager.fragments[0]
                .childFragmentManager.fragments[0].arguments
            val args = GameFragmentArgs.fromBundle(bundle!!)
            assertThat(args.gameDifficulty, `is`(GameDifficulty.EASY))
        }
    }

    @Test
    fun check_GameFragment_is_opened_with_MEDIUM_argument_correctly() {
        onView(withId(R.id.button_welcome_medium)).perform(click())
        activityScenarioRule.scenario.onActivity {
            val bundle = it.supportFragmentManager.fragments[0]
                .childFragmentManager.fragments[0].arguments
            val args = GameFragmentArgs.fromBundle(bundle!!)
            assertThat(args.gameDifficulty, `is`(GameDifficulty.MEDIUM))
        }
    }

    @Test
    fun check_GameFragment_is_opened_with_HARD_argument_correctly() {
        onView(withId(R.id.button_welcome_hard)).perform(click())
        activityScenarioRule.scenario.onActivity {
            val bundle = it.supportFragmentManager.fragments[0]
                .childFragmentManager.fragments[0].arguments
            val args = GameFragmentArgs.fromBundle(bundle!!)
            assertThat(args.gameDifficulty, `is`(GameDifficulty.HARD))
        }
    }
}