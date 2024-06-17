package com.example.playlistmaker.search.domain.interfaces

import android.widget.EditText
import com.example.playlistmaker.search.ui.SearchActivity

interface SearchInteractor {
    fun showResult()
    fun showResult (result : SearchActivity.SearchForResults)

    fun trackSearch()

    fun hideKeyboard(editText: EditText)

    fun searchDebounce()

    fun clickToTrack()

    fun clickDebounce()
}