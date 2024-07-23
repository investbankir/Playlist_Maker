package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SearchState.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors



class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {

    private val _state = MutableLiveData<SearchState>()
    val state: LiveData<SearchState> get() = _state

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks

    private val executor: Executor = Executors.newSingleThreadExecutor()

    fun searchTracks(query: String) {
        _state.value = LOADING
        executor.execute {
            searchInteractor.searchTracks(query, object : SearchInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, isFailed: Boolean?) {
                    if (foundTracks != null) {
                        _tracks.postValue(foundTracks)
                        _state.postValue(if (foundTracks.isEmpty()) {
                            NOTHING_FOUND
                        } else {
                            CONTENT
                        })
                    } else {
                        _state.postValue(COMMUNICATION_PROBLEMS)
                    }
                }
            })
        }
    }

    fun getSearchHistory() {
        val history = historyInteractor.getSearchHistory()
        _tracks.value = history
        if (history.isEmpty()) {
            _state.value = HISTORY_EMPTY
        } else {
            _state.value = HISTORY
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

    fun searchDebounced(query: String) {
        _state.value = LOADING
        executor.execute {
            searchInteractor.searchTracks(query, object : SearchInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, isFailed: Boolean?) {
                    if (foundTracks != null) {
                        _tracks.postValue(foundTracks)
                        _state.postValue(if (foundTracks.isEmpty()) {
                            NOTHING_FOUND
                        } else {
                            CONTENT
                        })
                    } else {
                        _state.postValue(COMMUNICATION_PROBLEMS)
                    }
                }
            })
        }
    }

}