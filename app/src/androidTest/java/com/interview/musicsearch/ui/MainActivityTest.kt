package com.interview.musicsearch.ui

import android.widget.AutoCompleteTextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.interview.musicsearch.R
import com.interview.musicsearch.ui.artist_albums.ArtistAlbumsAdapter
import com.interview.musicsearch.ui.search_artists.SearchArtistAdapter
import com.interview.musicsearch.util.EspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@LargeTest
@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    @get:Rule(order = 1)
    var mainActivityTestRule = ActivityScenarioRule(MainActivity::class.java)


    @Before
    fun setUp() {
        hiltRule.inject()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }


    @Test
    fun testCompleteFlow() {

        val queryText = "metallica"

        onView(isAssignableFrom(AutoCompleteTextView::class.java)).perform(replaceText(queryText))

        onView(withId(R.id.search_artist_recycler_view)).perform(
            actionOnItemAtPosition<SearchArtistAdapter.ArtistItemViewHolder>(
                1,
                click()
            )
        )
        onView(withId(R.id.albums_recycler_view)).check(matches(isDisplayed()))

        onView(withId(R.id.albums_recycler_view)).perform(
            actionOnItemAtPosition<ArtistAlbumsAdapter.AlbumItemViewHolder>(
                2,
                click()
            )
        )

        onView(withId(R.id.tracks_recycler_view)).check(matches(isDisplayed()))

        Espresso.pressBack()
        Espresso.pressBack()

        onView(isAssignableFrom(AutoCompleteTextView::class.java)).check(matches(withText(queryText)))


    }

}