package com.example.playlistmaker.createNewPlaylist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.createNewPlaylist.domain.models.Playlist
import com.example.playlistmaker.createNewPlaylist.domain.db.CreateNewPlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class PlaylistAddedState {
    SUCCESS,
    EMPTY
}
open class CreateNewPlaylistViewModel(private val interactor: CreateNewPlaylistInteractor): ViewModel() {
    var isCreateNewPlaylistFragmentFiled = false
    var artworkUri: String = ""
    var playlistName: String = ""
    var playlistDescription: String = ""

    private val _isAddedPlaylist = MutableLiveData<PlaylistAddedState?>(null)
    val isAddedPlaylist: LiveData<PlaylistAddedState?> get() = _isAddedPlaylist

    fun setUri(uri: String) {
        artworkUri = uri.toString()
        filledIn()
    }
    fun addNewPlaylist (artworkUri: String) {
        val playlist = Playlist (
            0,
            playlistName,
            playlistDescription.ifEmpty { null },
            if (artworkUri.isNotEmpty()) interactor.saveCoverPlaylist(artworkUri) else null,
            mutableListOf(),
            0,
            0,
        )
        viewModelScope.launch(Dispatchers.IO) {
            interactor.addNewPlaylist(playlist).collect {
                if (it > 0) {
                    _isAddedPlaylist.postValue(PlaylistAddedState.SUCCESS)
                } else {
                    _isAddedPlaylist.postValue(PlaylistAddedState.EMPTY)
                }
            }
        }
        isCreateNewPlaylistFragmentFiled = false
    }
    fun resetPlaylistAddedState() {
        _isAddedPlaylist.value = null
    }

    open fun filledIn() {
        isCreateNewPlaylistFragmentFiled = playlistName.isNotEmpty() || playlistDescription.isNotEmpty() || artworkUri.isNotEmpty()
    }
}