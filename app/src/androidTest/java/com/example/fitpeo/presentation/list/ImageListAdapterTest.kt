package com.example.fitpeo.presentation.list

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.fitpeo.R
import com.example.fitpeo.presentation.FragmentContainerActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.internal.matchers.TypeSafeMatcher
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ImageListAdapterTest {

    private lateinit var idlingResource: IdlingResource


    @Before
    fun setUp() {
        // Initialize the CountingIdlingResource
        idlingResource = CountingIdlingResource("AlbumListFragmentIdlingResource")

        // Launch the FragmentContainerActivity
        val activityScenario = ActivityScenario.launch(FragmentContainerActivity::class.java)

        // Register the IdlingResource
        activityScenario.onActivity { activity ->
            val fragment = AlbumListFragment()
            val fragmentManager = activity.supportFragmentManager
            fragmentManager.beginTransaction()
                .add(R.id.content_fragment, fragment, "AlbumListFragment")
                .commitNow()

            // Register the IdlingResource
            idlingResource = fragment.getIdlingResource()
            IdlingRegistry.getInstance().register(idlingResource)
        }

    }

    @After
    fun tearDown() {
        // Unregister the IdlingResource
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Test
    fun testImageListAdapter() {
        // Delay for initial loading
        Espresso.onView(withIndex(withId(R.id.albumprogressBar),1)).check(matches(isDisplayed()))
        Thread.sleep(2000)
        // Verify that the list is displayed
        Espresso.onView(withIndex(withId(R.id.albumrv),1)).check(matches(isDisplayed()))
        Thread.sleep(2000)

        // Perform a click on the first item
        Espresso.onView(withIndex(withId(R.id.albumrv),1)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ImageListAdapter.ImageHolder>(0, click()))
        Thread.sleep(2000)

        // Verify that the detail screen is displayed
        //Espresso.onView(withIndex(withId(R.id.album_detail_view),1)).check(matches(isDisplayed()))
    }

    private fun AlbumListFragment.getIdlingResource(): IdlingResource {
        return object : IdlingResource {
            override fun getName(): String {
                return "AlbumListFragmentIdlingResource"
            }

            override fun isIdleNow(): Boolean {
                return true
            }

            override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
                // Not needed for this test
            }
        }
    }


    companion object {
        private const val TEST_ALBUM_ID = 1
        private const val TEST_ALBUM_TITLE = "Test Album"
        private const val TEST_ALBUM_URL = "http://example.com/image.jpg"
        private const val TEST_ALBUM_THUMBNAIL_URL = "http://example.com/thumbnail.jpg"
        private const val TEST_ALBUM_IS_FAVORITE = true
        private const val TEST_ALBUM_POSITION = 0
    }

    fun withIndex(matcher: Matcher<View?>, index: Int): Matcher<View?> {
        return object : TypeSafeMatcher<View?>() {
            var currentIndex = 0
            override fun describeTo(description: Description) {
                description.appendText("with index: ")
                description.appendValue(index)
                matcher.describeTo(description)
            }

            override fun matchesSafely(view: View?): Boolean {
                return matcher.matches(view) && currentIndex++ == index
            }
        }
    }
}
