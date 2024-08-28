package com.example.playlistmaker.search.data.repository

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.api.HistoryRepository
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class HistoryRepositoryImpl(
    private val gson: Gson,
    private val sharedPrefs : SharedPreferences
    ) : HistoryRepository {
    companion object {
        const val KEY_SEARCH_HISTORY = "key_search_history"
        const val MAX_HISTORY_SIZE = 10
    }
    override fun write(searchHistoryTrack: List<Track>) {
        val json = gson.toJson(searchHistoryTrack)
        sharedPrefs.edit()
            .putString(KEY_SEARCH_HISTORY, json)
            .apply()
    }

    override fun read(): List<Track> {
        val json = sharedPrefs.getString(KEY_SEARCH_HISTORY, null)
        return if (!json.isNullOrEmpty()) {
            val typeToken = object : TypeToken<List<Track>>() {}.type
            gson.fromJson(json, typeToken)
        } else {
            emptyList()
        }
    }

    override fun clearSearchHistory() {
        write(emptyList())
    }

    override fun addTrack(track: Track) {
        val searchHistoryTrack = read().toMutableList()

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