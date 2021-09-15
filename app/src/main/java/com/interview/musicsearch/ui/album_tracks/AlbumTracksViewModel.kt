package com.interview.musicsearch.ui.album_tracks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interview.musicsearch.data.ContentRepository
import com.interview.musicsearch.data.DataError
import com.interview.musicsearch.data.model.Album
import com.interview.musicsearch.data.model.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AlbumTracksViewModel @Inject constructor(
    private val repository: ContentRepository
) : ViewModel() {

    private val _spinnerLiveData = MutableLiveData(false)
    private val _snackBarLiveData = MutableLiveData<String?>()
    private val _albumTracksLiveData = MutableLiveData<List<DataItem>>()

    val albumTracksLiveData: LiveData<List<DataItem>>
        get() = _albumTracksLiveData

    val snackbarLiveData: LiveData<String?>
        get() = _snackBarLiveData

    val spinnerLiveData: LiveData<Boolean>
        get() = _spinnerLiveData

    fun getAlbumTracks(id: String) {
        viewModelScope.launch {
            try {
                _spinnerLiveData.value = true

                val album = repository.fetchAlbum(id)
                val tracks = repository.fetchAlbumTracks(id)

                withContext(Dispatchers.Default) {
                    val items = prepareDataItems(album, tracks)
                    _albumTracksLiveData.postValue(items)
                }
            } catch (error: DataError) {
                _snackBarLiveData.value = error.message
            } finally {
                _spinnerLiveData.value = false
            }
        }
    }

    private fun prepareDataItems(album: Album, tracks: List<Track>): List<DataItem> {
        val items = mutableListOf<DataItem>()
        val sorted = tracks.sortedWith(compareBy({ it.disk_number }, { it.track_position }))

        var currentDiskNumber = 0

        sorted.forEach {
            if (it.disk_number != currentDiskNumber) {
                currentDiskNumber = it.disk_number
                items.add(DataItem.DiskItem("Volume ${it.disk_number}"))
            }
            items.add(DataItem.TrackerItem(it))
        }

        if (currentDiskNumber == 1) {
            items.removeAt(0)
        }

        items.add(0, DataItem.AlbumItem(album))
        return items
    }


    fun resetSnackbar() {
        _snackBarLiveData.value = null
    }


}