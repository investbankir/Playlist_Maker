package com.example.playlistmaker.selected_playlist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media_library.domain.db.PlaylistsInteractor
import com.example.playlistmaker.createNewPlaylist.domain.db.CreateNewPlaylistInteractor
import com.example.playlistmaker.selected_playlist.domain.api.SelectedPlaylistInteractor
import com.example.playlistmaker.createNewPlaylist.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.selected_playlist.ui.PlaylistDeleteState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectedPlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val createNewPlaylistInteractor: CreateNewPlaylistInteractor,
    private val selectedPlaylistInteractor: SelectedPlaylistInteractor
): ViewModel() {

    private var playlist: Playlist? = null

    private var playlistLiveData = MutableLiveData<Playlist?>(playlist)
    fun getPlaylistLiveData(): LiveData<Playlist?> = playlistLiveData

    private var tracksFromPlaylistsLiveData = MutableLiveData(listOf<Track>())
    fun getTracksFromPlaylistsLiveData() : LiveData<List<Track>> = tracksFromPlaylistsLiveData
    private var deletePlaylistLiveData = MutableLiveData<PlaylistDeleteState>()
    fun getStateDelete(): LiveData<PlaylistDeleteState> = deletePlaylistLiveData

    fun getPlaylistById(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            selectedPlaylistInteractor
                .getPlaylistById(playlistId)
                .collect {playlist ->
                    playlistLiveData.postValue(playlist)
                    selectedPlaylistInteractor
                        .getTracksFromPlaylists(playlist.tracksList)
                        .collect { tracksFromPlaylistsLiveData.postValue(it)}
            }
        }
    }

    fun getTracksFromPlaylists(playlistIdList: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            selectedPlaylistInteractor
                .getTracksFromPlaylists(playlistIdList)
                .collect {tracksFromPlaylistsLiveData.postValue(it)}
        }
    }

    fun updatePlaylist(playlist: Playlist, track: Track) {
        val trackId = track.trackId
        playlist.tracksList.remove(trackId)
        viewModelScope.launch(Dispatchers.IO) {
            createNewPlaylistInteractor
                .updatePlaylist(playlist)
                .collect{}
        }
    }
    fun deleteTrackById(trackId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor
                .getPlaylists()
                .collect {
                    if (checkTrack(trackId, it)) {
                        playlistsInteractor.deleteTrackById(trackId)
                    }
                }
        }
    }

    fun deletePlaylistById(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            createNewPlaylistInteractor
                .deletePlaylistById(playlistId)
                .collect { stateDelete ->
                    if (stateDelete == 1L) deletePlaylistLiveData.postValue(PlaylistDeleteState(true))
                }
        }
    }
    fun rightEndingTracks(quantityTracks: Int): String{
        val ending = when (quantityTracks % 10) {
            1 -> " трек"
            2, 3, 4, -> " трека"
            else -> " треков"
        }
        return quantityTracks.toString() + ending
    }

    private fun checkTrack (trackId: Int, list: List<Playlist>): Boolean {
        for (playlist: Playlist in list) {
            if (!playlist.tracksList.none { it == trackId }) return false
        }
        return true
    }
}