package com.interview.musicsearch.ui.album_tracks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.interview.musicsearch.MainCoroutineRule
import com.interview.musicsearch.data.ContentRepository
import com.interview.musicsearch.data.model.Album
import com.interview.musicsearch.data.model.Track
import com.interview.musicsearch.getOrAwaitValueTest
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AlbumTracksViewModelTest {
    private lateinit var viewModel: AlbumTracksViewModel
    private lateinit var repository: ContentRepository

    private lateinit var items: List<DataItem>

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val album = Album("", "", "", "", "")
    private val tracks = listOf(
        Track("1", "", 1, 1),
        Track("2", "", 1, 1),
        Track("3", "", 1, 2),
        Track("4", "", 1, 3)
    )


    @Before
    fun setUp() {
        repository = mockk()
        coEvery { repository.fetchAlbum(any()) } returns album
        coEvery { repository.fetchAlbumTracks(any()) } returns tracks
        viewModel = AlbumTracksViewModel(repository)

        viewModel.getAlbumTracks("")
        items = viewModel.albumTracksLiveData.getOrAwaitValueTest()
    }

    @Test
    fun `live data has correct number of items`() {
        Truth.assertThat(items.size).isEqualTo(8)
    }

    @Test
    fun `disk item has correct content`() {
        Truth.assertThat(items[4].id).isEqualTo("Volume 2")
    }

    @Test
    fun `track item has correct content`() {
        Truth.assertThat(items[5].id).isEqualTo("3")
    }

    @Test
    fun `The first item is always the Album item`() {
        Truth.assertThat(items[0].id).isEqualTo("Album")
    }

}