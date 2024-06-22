package com.example.playlistmaker.search.data

import com.example.playlistmaker.App
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

//class SearchHistory {
    companion object {
        const val KEY_SEARCH_HISTORY = "key_search_history"
        const val MAX_HISTORY_SIZE = 10
        fun write(searchHistoryTrack: ArrayList<Track>) {
            val json = Gson().toJson(searchHistoryTrack)
            App.sharedPrefs.edit()
                .putString(KEY_SEARCH_HISTORY, json)
                .apply()
        }

        fun read(): ArrayList<Track> {
            var searchHistoryTrack = ArrayList<Track>()
            val json = App.sharedPrefs.getString(KEY_SEARCH_HISTORY, null)
            if (!json.isNullOrEmpty()) {
                val typeToken = object : TypeToken<ArrayList<Track>>() {}.type
                searchHistoryTrack = Gson().fromJson(json, typeToken)
            }
            return searchHistoryTrack
        }

        fun clearSearchHistory() {
            write(ArrayList())
        }
    }
    fun addTrack(track: Track) {
        val searchHistoryTrack = read()

        if (searchHistoryTrack.contains(track)) {
            searchHistoryTrack.remove(track)
        }
        if (searchHistoryTrack.size >= MAX_HISTORY_SIZE) {
            searchHistoryTrack.removeAt(searchHistoryTrack.size - 1)
        }
        searchHistoryTrack.add(0, track)
        write(searchHistoryTrack)

    }
}