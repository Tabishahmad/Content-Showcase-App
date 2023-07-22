package com.example.fitpeo.view

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.fitpeo.R
import com.example.fitpeo.presentation.FragmentContainerActivity
import com.example.fitpeo.presentation.splash.SplashActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.example.fitpeo.presentation.list.AlbumListFragment
import org.hamcrest.CoreMatchers
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
@LargeTest
class CompleteApplicationFlowTest {
    @get:Rule
    val intentsTestRule = IntentsTestRule(SplashActivity::class.java)
    @Before
    fun setUp() {
    }
    @Test
    fun completeFlowTest() {
        // Launch the activity
        ActivityScenario.launch(SplashActivity::class.java)


        // Verify that the splash screen is displayed
        onView(withId(R.id.splash_screen)).check(matches(isDisplayed()))

        // Perform a click on the "startListFragment" button
        onView(withId(R.id.startListFragment)).perform(click())

        // Verify that the MainActivity is displayed after the splash screen
        onView(withId(R.id.activity_main)).check(matches(isDisplayed()))

        // Assert that the next activity is launched
        intended(IntentMatchers.hasComponent(FragmentContainerActivity::class.java.name))

        // Verify that the list is displayed within the fragment
        onView(withId(R.id.rv)).check(matches(isDisplayed()))

        // Delay for 2000ms
        Thread.sleep(2000)

        onView(withId(R.id.rv))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // Check if a view within AlbumDetailFragment is displayed
        onView(withId(R.id.album_detail_view)).check(matches(isDisplayed()))

        // Check if the TextView is displayed
        onView(withId(R.id.textView)).check(matches(isDisplayed()))

        // Check if the ImageView is displayed
        onView(withId(R.id.imageView)).check(matches(isDisplayed()))
    }
    @After
    public fun close() {

    }
}