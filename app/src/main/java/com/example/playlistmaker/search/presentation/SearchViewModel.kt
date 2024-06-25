package com.example.playlistmaker.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.models.Track
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
        _state.value = SearchState.LOADING

        executor.execute {
            searchInteractor.searchTracks(query, object : SearchInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, isFailed: Boolean?) {
                    if (foundTracks != null) {
                        _tracks.postValue(foundTracks)
                        _state.postValue(if (foundTracks.isEmpty()) {
                            SearchState.NOTHING_FOUND
                        } else {
                            SearchState.CONTENT
                        })
                    } else {
                        _state.postValue(SearchState.COMMUNICATION_PROBLEMS)
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

    fun searchDebounced(query: String) {
        executor.execute {
            searchInteractor.searchTracks(query, object : SearchInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, isFailed: Boolean?) {
                    if (foundTracks != null) {
                        _tracks.postValue(foundTracks)
                        _state.postValue(if (foundTracks.isEmpty()) {
                            SearchState.NOTHING_FOUND
                        } else {
                            SearchState.CONTENT
                        })
                    } else {
                        _state.postValue(SearchState.COMMUNICATION_PROBLEMS)
                    }
                }
            })
        }
    }

}