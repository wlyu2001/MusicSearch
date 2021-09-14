package com.interview.musicsearch.ui.album_tracks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.interview.musicsearch.data.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlbumTracksViewModel @Inject constructor(
    private val repository: ContentRepository,
    private val state: SavedStateHandle
) : ViewModel() {


}