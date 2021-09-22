package com.interview.musicsearch.ui.search_artists

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.interview.musicsearch.MainCoroutineRule
import com.interview.musicsearch.api.FakeRepository
import com.interview.musicsearch.getOrAwaitValueTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchArtistsViewModelTest {
    private lateinit var viewModel: SearchArtistsViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: FakeRepository


    @Before
    fun setUp() {
        repository = FakeRepository()
        viewModel = SearchArtistsViewModel(repository)
    }


    @Test
    fun `show spinner during search`() {
        var spinnerValue = viewModel.spinnerLiveData.getOrAwaitValueTest()
        Truth.assertThat(spinnerValue).isFalse()

        viewModel.searchArtists("search query")

        spinnerValue = viewModel.spinnerLiveData.getOrAwaitValueTest()
        Truth.assertThat(spinnerValue).isTrue()

        repository.sendCompletionToAllCurrentRequests()

        spinnerValue = viewModel.spinnerLiveData.getOrAwaitValueTest()
        Truth.assertThat(spinnerValue).isFalse()

    }

    @Test
    fun `show error when there is data error and hide spinner`() {

        viewModel.searchArtists("search query")

        var snackbarValue = viewModel.snackbarLiveData.value
        Truth.assertThat(snackbarValue).isNull()

        repository.sendErrorToCurrentRequests()

        snackbarValue = viewModel.snackbarLiveData.getOrAwaitValueTest()
        Truth.assertThat(snackbarValue).isNotNull()

        val spinnerValue = viewModel.spinnerLiveData.getOrAwaitValueTest()
        Truth.assertThat(spinnerValue).isFalse()

    }

    @Test
    fun `query text is correctly stored in view model`() {

        val queryText = "search query"
        viewModel.searchArtists(queryText)

        val queryTextValue = viewModel.searchQueryLiveData.value
        Truth.assertThat(queryTextValue).isEqualTo(queryText)

    }

}