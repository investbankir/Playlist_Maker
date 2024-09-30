package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SearchState.*
import kotlinx.coroutines.launch



class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {

    private val _state = MutableLiveData<SearchState>()
    val state: LiveData<SearchState> get() = _state

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks

    fun searchTracks(query: String) {
        _state.value = LOADING
        viewModelScope.launch {
            searchInteractor.searchTracks(query).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val foundTracks = result.data ?: emptyList()
                        _tracks.postValue(foundTracks)
                        _state.postValue(
                            if (foundTracks.isEmpty()) NOTHING_FOUND else CONTENT)
                    }
                    is Resource.Error -> {
                        _state.postValue(COMMUNICATION_PROBLEMS)
                    }
                }
            }
        }
    }

    fun getSearchHistory() {
        val history = historyInteractor.getSearchHistory()
        _tracks.value = history
        _state.value = if (history.isEmpty()) HISTORY_EMPTY else HISTORY
    }

    fun clearHistory() {
        historyInteractor.clearHistory()
        getSearchHistory()
    }

    fun addTrackHistory(track: Track) {
        historyInteractor.addTrackToHistory(track)
        getSearchHistory()
    }
}