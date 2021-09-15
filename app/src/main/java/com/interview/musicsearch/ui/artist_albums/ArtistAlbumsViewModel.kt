package com.interview.musicsearch.ui.artist_albums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interview.musicsearch.data.ContentRepository
import com.interview.musicsearch.data.DataError
import com.interview.musicsearch.data.model.Album
import com.interview.musicsearch.data.model.SimpleArtist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ArtistAlbumsViewModel @Inject constructor(
    private val repository: ContentRepository
) : ViewModel() {

    private val _spinnerLiveData = MutableLiveData(false)
    private val _snackBarLiveData = MutableLiveData<String?>()
    private val _artistAlbumsLiveData = MutableLiveData<List<Album>>()

    val artistAlbumsLiveData: LiveData<List<Album>>
        get() = _artistAlbumsLiveData

    val snackbarLiveData: LiveData<String?>
        get() = _snackBarLiveData

    val spinnerLiveData: LiveData<Boolean>
        get() = _spinnerLiveData


    fun getArtistAlbums(id: String) {
        viewModelScope.launch {
            try {
                _spinnerLiveData.value = true
                val artist = SimpleArtist(repository.fetchArtist(id).name)
                val albums = repository.fetchArtistAlbums(id)
                albums.map { it.artist = artist }
                _artistAlbumsLiveData.value = albums
            } catch (error: DataError) {
                _snackBarLiveData.value = error.message
            } finally {
                _spinnerLiveData.value = false
            }
        }
    }

    fun resetSnackbar() {
        _snackBarLiveData.value = null
    }
}