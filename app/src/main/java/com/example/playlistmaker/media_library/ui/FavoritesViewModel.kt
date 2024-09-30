package com.example.playlistmaker.media_library.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import  com.example.playlistmaker.media_library.domain.db.FavoriteInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavoritesViewModel(private val favoriteInteractor: FavoriteInteractor
): ViewModel() {

    private var favoriteLiveData = MutableLiveData(listOf<Track>())
    fun getFavoriteLiveData(): LiveData<List<Track>> = favoriteLiveData

    fun getFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteInteractor
                .getTracks()
                .collect {favoriteLiveData.postValue(checkingFavorites(it))}
        }
    }

    private fun checkingFavorites(tracks: List<Track>): List<Track> {
        tracks.forEach { track -> track.isFavorite = true }
        return tracks
    }
}