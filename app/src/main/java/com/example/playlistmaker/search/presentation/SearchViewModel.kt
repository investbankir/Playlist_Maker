package com.example.playlistmaker.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Track>>()
    val searchResults : LiveData<List<Track>> get() = _searchResults

    private val _searchHistory = MutableLiveData<List<Track>>()
    val searchHistory: LiveData<List<Track>> get() = _searchHistory

    //private val _isLoading = MutableLiveData<Boolean>()
    //val isLoading: LiveData<Boolean> get() = _isLoading


    //private val _isError = MutableLiveData<Boolean>()
    //val isError: LiveData<Boolean> get() = _isError
    fun loadSearchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            _searchHistory.postValue(historyInteractor.getSearchHistory())
        }
    }

    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            historyInteractor.clearHistory()
            _searchHistory.postValue(emptyList())
        }
    }

    fun addTrack(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            historyInteractor.addTrackToHistory(track)
            _searchHistory.postValue(historyInteractor.getSearchHistory())
        }
    }

}