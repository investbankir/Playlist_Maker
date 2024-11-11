package com.example.playlistmaker.media_library.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.createNewPlaylist.domain.models.Playlist
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.media_library.domain.db.PlaylistsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PlaylistViewModel(private val interactor: PlaylistsInteractor): ViewModel() {
    private val _playlists = MutableLiveData(listOf<Playlist>())
    val playlists: LiveData<List<Playlist>> get() = _playlists

    fun getPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getPlaylists().collect{_playlists.postValue(it)}
        }
    }
}