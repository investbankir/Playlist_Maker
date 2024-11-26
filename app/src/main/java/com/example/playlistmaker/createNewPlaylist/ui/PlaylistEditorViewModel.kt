package com.example.playlistmaker.createNewPlaylist.ui

import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.createNewPlaylist.domain.models.Playlist
import com.example.playlistmaker.createNewPlaylist.domain.db.CreateNewPlaylistInteractor
import kotlin.collections.MutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistEditorViewModel(
    private val interactor: CreateNewPlaylistInteractor
): CreateNewPlaylistViewModel(interactor) {

    var quantityTracks: Int = 0
    var totalPlaylistTime: Int = 0
    var endingMinute: String = "минут"
    override fun filledIn() { }

    //Полечить отображение данных в редактируемом ПЛ, разобраться с высотой БШ

    fun savePlaylist(editablePlaylistId: Long?, editableArtworkUri: String?,editableTracksList: MutableList<Int>?) {
        val playlist = Playlist (
            editablePlaylistId!!,
            playlistName,
            playlistDescription.ifEmpty { null },
            if (artworkUri.isNotEmpty()) interactor.saveCoverPlaylist(artworkUri) else editableArtworkUri,
            editableTracksList?: mutableListOf(),
            quantityTracks,
            totalPlaylistTime,
            endingMinute
        )

        viewModelScope.launch(Dispatchers.IO) {
            interactor
                .updatePlaylist(playlist)
                .collect{}
        }
        isCreateNewPlaylistFragmentFiled = false
    }
}