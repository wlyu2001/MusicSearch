package com.interview.musicsearch.ui.search_artists

import androidx.lifecycle.*
import com.interview.musicsearch.data.DataError
import com.interview.musicsearch.data.ContentRepository
import com.interview.musicsearch.data.model.Artist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


@HiltViewModel
class SearchArtistsViewModel @Inject constructor(
    private val repository: ContentRepository,
    private val state: SavedStateHandle
) : ViewModel() {


    private val _spinnerLiveData = MutableLiveData(false)
    private val _snackBarLiveData = MutableLiveData<String?>()

    private val _queryTextLiveData = MutableLiveData<String>()

    fun setQueryText(queryText: String) {
        if(!queryText.isEmpty()) {
            _queryTextLiveData.value = queryText
            _spinnerLiveData.value = true
        } else {
            _queryTextLiveData.value = ""
        }
    }

    val searchArtistsLiveData: LiveData<List<Artist>> = _queryTextLiveData.switchMap { queryText ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            try {
                emit(repository.searchArtists(queryText))
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