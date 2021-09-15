package com.interview.musicsearch.ui.album_tracks

import androidx.lifecycle.*
import com.interview.musicsearch.data.ContentRepository
import com.interview.musicsearch.data.DataError
import com.interview.musicsearch.data.model.Album
import com.interview.musicsearch.data.model.Artist
import com.interview.musicsearch.data.model.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
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

    private fun prepareDataItems(album: Album, tracks: List<Track>): List<DataItem> {
        val items = mutableListOf<DataItem>()
        val sorted = tracks.sortedWith(compareBy({ it.disk_number }, { it.track_position }))

        var currentDiskNumber = 0

        sorted.forEach {
            if (it.disk_number != currentDiskNumber) {
                currentDiskNumber = it.disk_number
                items.add(DataItem.DiskItem("Volume${it.disk_number}"))
            }
            items.add(DataItem.TrackerItem(it))
        }

        if(currentDiskNumber == 1) {
            items.removeAt(0)
        }

        items.add(0, DataItem.AlbumItem(album))
        return items
    }

    val albumTracksLiveData: LiveData<List<DataItem>> = _albumIdLiveData.switchMap { id ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            try {
                val album = repository.fetchAlbum(id)
                val tracks = repository.fetchAlbumTracks(id)

                withContext(Dispatchers.Default) {
                    val items = prepareDataItems(album, tracks)
                    emit(items.toList())
                }


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