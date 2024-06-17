package com.example.playlistmaker.search.presentation

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.interfaces.HistoryInteractor
import com.example.playlistmaker.search.domain.interfaces.SearchInteractor

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {

}