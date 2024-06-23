package com.example.playlistmaker.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.models.Track
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
        _state.value = SearchState.LOADING
        viewModelScope.launch {
            searchInteractor.searchTracks(query, object : SearchInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, isFailed: Boolean?) {
                    if (foundTracks != null) {
                        _tracks.value = foundTracks!!// Пришлось проставить !! так как студия подсвечивала ошибку
                        _state.value = if (foundTracks.isEmpty()) {
                            SearchState.NOTHING_FOUND
                        } else {
                            SearchState.CONTENT
                        }
                    } else {
                        _state.value = SearchState.COMMUNICATION_PROBLEMS
                    }
                }
            })
        }
    }

    fun getSearchHistory() {
        _tracks.value = historyInteractor.getSearchHistory()
        _state.value = if (_tracks.value.isNullOrEmpty()) {
            SearchState.NOTHING_FOUND
        } else {
            SearchState.HISTORY
        }
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