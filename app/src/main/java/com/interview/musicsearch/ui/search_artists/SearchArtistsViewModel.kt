package com.interview.musicsearch.ui.search_artists

import androidx.lifecycle.*
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

    private val _searchQueryLiveData = MutableLiveData<String>()
    private val _searchArtistsLiveData = MutableLiveData<List<Artist>>()

    fun searchArtists(queryText: String) {
        if (_searchQueryLiveData.value != queryText) {
            _spinnerLiveData.value = queryText.isNotEmpty()
            _searchQueryLiveData.value = queryText
            viewModelScope.launch {
                try {
                    _searchArtistsLiveData.value = repository.searchArtists(queryText)
                } catch (error: DataError) {
                    _snackBarLiveData.value = error.message
                } finally {
                    _spinnerLiveData.value = false
                }
            }
        }
    }

    val searchQueryLiveData: LiveData<String>
        get() = _searchQueryLiveData

    val searchArtistsLiveData: LiveData<List<Artist>>
        get() = _searchArtistsLiveData

    val snackbarLiveData: LiveData<String?>
        get() = _snackBarLiveData

    val spinnerLiveData: LiveData<Boolean>
        get() = _spinnerLiveData

    fun resetSnackbar() {
        _snackBarLiveData.value = null
    }


}