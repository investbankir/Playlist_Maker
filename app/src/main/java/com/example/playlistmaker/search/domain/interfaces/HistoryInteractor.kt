package com.example.playlistmaker.search.domain.interfaces

import com.example.playlistmaker.App
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

interface HistoryInteractor {
    fun write(searchHistoryTrack: ArrayList<Track>)
    fun read()
    fun clearSearchHistory()
    fun addTrack(track: Track)
}