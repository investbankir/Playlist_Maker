package com.example.playlistmaker.selected_playlist.domain.api

import com.example.playlistmaker.createNewPlaylist.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SelectedPlaylistInteractor {
    fun getPlaylistById(playlistId: Long): Flow<Playlist>
    fun getTracksFromPlaylists(playlistIdList: List<Int>): Flow<List<Track>>
    fun deletePlaylistById(playlistId:Long)
}
