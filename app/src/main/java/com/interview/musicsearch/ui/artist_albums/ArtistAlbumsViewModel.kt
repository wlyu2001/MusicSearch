package com.interview.musicsearch.ui.artist_albums

import androidx.lifecycle.*
import com.interview.musicsearch.data.ContentRepository
import com.interview.musicsearch.data.DataError
import com.interview.musicsearch.data.model.Album
import com.interview.musicsearch.data.model.Artist
import com.interview.musicsearch.data.model.SimpleArtist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


@HiltViewModel
class ArtistAlbumsViewModel @Inject constructor(
    private val repository: ContentRepository,
    private val state: SavedStateHandle
) : ViewModel() {


    private val _spinnerLiveData = MutableLiveData(false)
    private val _snackBarLiveData = MutableLiveData<String?>()

    private val _artistIdLiveData = MutableLiveData<String>()

    fun getArtistAlbums(id: String) {
        _artistIdLiveData.value = id
        _spinnerLiveData.value = true
    }

    val artistAlbumsLiveData: LiveData<List<Album>> = _artistIdLiveData.switchMap { id ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            try {
                val artist = SimpleArtist(repository.fetchArtist(id).name)
                val albums = repository.fetchArtistAlbums(id)
                albums.map { it.artist = artist }
                emit(albums)

            } catch (error: DataError) {
                _snackBarLiveData.postValue(error.message)
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