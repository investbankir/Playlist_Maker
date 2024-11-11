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
class CreateNewPlaylistViewModel(private val interactor: CreateNewPlaylistInteractor): ViewModel() {
    var isCreateNewPlaylistFragmentFiled = false
    var playlistUri: String = ""
    var playlistName: String = ""
    var playlistDescription: String = ""

    private val _isAddedPlaylist = MutableLiveData<PlaylistAddedState?>(null)
    val isAddedPlaylist: LiveData<PlaylistAddedState?> get() = _isAddedPlaylist

    fun setUri(uri: String) {
        playlistUri = uri.toString()
        filledIn()
    }
    fun addNewPlaylist (plalistUri: String) {
        val playlist = Playlist (
            0,
            playlistName,
            playlistDescription.ifEmpty { null },
            if (plalistUri.isNotEmpty()) interactor.saveCoverPlaylist(plalistUri) else null,
            mutableListOf(),
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

    fun filledIn() {
        isCreateNewPlaylistFragmentFiled = playlistName.isNotEmpty() || playlistDescription.isNotEmpty() || playlistUri.isNotEmpty()
    }
}