package com.interview.musicsearch.ui.search_artists

import androidx.lifecycle.*
import com.interview.musicsearch.data.DataError
import com.interview.musicsearch.data.ContentRepository
import com.interview.musicsearch.data.model.Artist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchArtistsViewModel @Inject constructor(
    private val repository: ContentRepository,
    private val state: SavedStateHandle
) : ViewModel() {


    private val _spinner = MutableLiveData(false)
    private val _snackBar = MutableLiveData<String?>()

    private val _searchTextLiveData = MutableLiveData<String>()

    fun setSearchText(text: String) {
        if(!text.isEmpty()) {
            _searchTextLiveData.value = text
            _spinner.value = true
        } else {
            _searchTextLiveData.value = ""
        }
    }

    val searchResultArtists: LiveData<List<Artist>> = _searchTextLiveData.switchMap { text ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            try {
                emit(repository.searchArtists(text))
            } catch (error: DataError) {
                _snackBar.value = error.message
                _snackBar.postValue("")
            } finally {
                _spinner.postValue(false)
            }
        }
    }

    val snackbar: LiveData<String?>
        get() = _snackBar

    val spinner: LiveData<Boolean>
        get() = _spinner

    fun resetSnackbar() {
        _snackBar.value = null
    }


}