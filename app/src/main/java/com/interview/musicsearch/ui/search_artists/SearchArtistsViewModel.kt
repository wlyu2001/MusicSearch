package com.interview.musicsearch.ui.search_artists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interview.musicsearch.data.ContentRepository
import com.interview.musicsearch.data.DataError
import com.interview.musicsearch.data.model.Artist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchArtistsViewModel @Inject constructor(
    private val repository: ContentRepository
) : ViewModel() {

    private val _spinnerLiveData = MutableLiveData(false)
    private val _snackBarLiveData = MutableLiveData<String?>()
    private val _searchArtistsLiveData = MutableLiveData<List<Artist>>()

    val searchArtistsLiveData: LiveData<List<Artist>>
        get() = _searchArtistsLiveData

    val snackbarLiveData: LiveData<String?>
        get() = _snackBarLiveData

    val spinnerLiveData: LiveData<Boolean>
        get() = _spinnerLiveData

    fun searchArtists(queryText: String) {
        viewModelScope.launch {
            try {
                _spinnerLiveData.value = true
                _searchArtistsLiveData.value = repository.searchArtists(queryText)
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