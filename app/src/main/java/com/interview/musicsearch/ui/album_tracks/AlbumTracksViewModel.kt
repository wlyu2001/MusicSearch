package com.interview.musicsearch.ui.album_tracks

import androidx.lifecycle.*
import com.interview.musicsearch.data.ContentRepository
import com.interview.musicsearch.data.DataError
import com.interview.musicsearch.data.model.Album
import com.interview.musicsearch.data.model.Artist
import com.interview.musicsearch.data.model.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class AlbumTracksViewModel @Inject constructor(
    private val repository: ContentRepository,
    private val state: SavedStateHandle
) : ViewModel() {


    private val _spinnerLiveData = MutableLiveData(false)
    private val _snackBarLiveData = MutableLiveData<String?>()

    private val _albumIdLiveData = MutableLiveData<String>()

    fun getAlbumTracks(id: String) {
        _albumIdLiveData.value = id
        _spinnerLiveData.value = true
    }

    val albumTracksLiveData: LiveData<Pair<Album, List<Track>>> = _albumIdLiveData.switchMap { id ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            try {
                val album = repository.fetchAlbum(id)
                val tracks = repository.fetchAlbumTracks(id)
                emit(Pair(album, tracks))

            } catch (error: DataError) {
                _snackBarLiveData.value = error.message
                _snackBarLiveData.postValue("")
            } finally {
                _spinnerLiveData.postValue(false)
            }
        }
    }

    val snackbarLiveData: LiveData<String?>
        get() = _snackBarLiveData

    val spinnerLiveData: LiveData<Boolean>
        get() = _spinnerLiveData

    fun resetSnackbar() {
        _snackBarLiveData.value = null
    }



}