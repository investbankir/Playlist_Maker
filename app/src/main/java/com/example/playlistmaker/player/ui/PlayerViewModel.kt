package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.createNewPlaylist.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import  com.example.playlistmaker.media_library.domain.db.FavoriteInteractor
import com.example.playlistmaker.media_library.domain.db.PlaylistsInteractor
import com.example.playlistmaker.createNewPlaylist.domain.db.CreateNewPlaylistInteractor
import com.example.playlistmaker.player.domain.models.PlayerStateStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoriteInteractor: FavoriteInteractor,
    private val playlistsInteractor: PlaylistsInteractor,
    private val createNewPlaylistInteractor: CreateNewPlaylistInteractor,
    private val previewUrl: String?
) : ViewModel() {
    companion object {
        private const val WAITING_TIME = 300L
    }
    private val _playerState = MutableLiveData<PlayerStateStatus>().apply {
        value = PlayerStateStatus.STATE_DEFAULT()
    }
    val playerState : LiveData<PlayerStateStatus> get() = _playerState

    private val _playlists = MutableLiveData(listOf<Playlist>())
    val playlists: LiveData<List<Playlist>> get() = _playlists

    private var favoriteLiveData = MutableLiveData<Boolean>()
    fun getFavoriteLiveData(): LiveData<Boolean> = favoriteLiveData


    private val _currentPosition = MutableLiveData<Int>().apply {
        value = 0
    }
    val currentPosition : LiveData<Int> get() = _currentPosition

    private val _addTrackResult = MutableLiveData<Pair<Boolean, String>>()
    val addTrackResult: LiveData<Pair<Boolean, String>> get() = _addTrackResult

    private var updatePlaylistLiveData = MutableLiveData("")
    fun getUpdatePlaylistLiveData(): LiveData<String> = updatePlaylistLiveData

    private var updatePositionJob: Job? = null
    var playlistName: String = ""

    fun updatePlaylist(playlist: Playlist, track: Track) {
        createNewPlaylistInteractor.addTrackInPlaylist(track)
        viewModelScope.launch(Dispatchers.IO) {
            val isAdded = playlist.tracksList.none { it == track.trackId }
            if (isAdded) {
                playlist.tracksList.add(track.trackId)
                createNewPlaylistInteractor.updatePlaylist(playlist).collect {
                    if (it == 1) _addTrackResult.postValue(Pair(true, playlist.playlistName ?: ""))
                }
            } else {
                _addTrackResult.postValue(Pair(false, playlist.playlistName ?: ""))
            }
        }
    }

    init {
        playerInteractor.setOnChangePlayerListener { state ->
            _playerState.postValue(state)
            when (state) {
                is PlayerStateStatus.STATE_PLAYING -> startUpdatingPosition()
                is PlayerStateStatus.STATE_PREPARED -> {
                    stopUpdatingPosition()
                    _currentPosition.postValue(0)
                }
                else -> stopUpdatingPosition()
            }
        }
        preparePlayer()
    }

    fun getPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.getPlaylists().collect{_playlists.postValue(it)}
        }
    }
    suspend fun onFavoriteClicked(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteInteractor.onFavoriteClicked(track)
            track.isFavorite = !track.isFavorite
            favoriteLiveData.postValue(track.isFavorite)
        }
    }
    private fun preparePlayer() {
        viewModelScope.launch {
            playerInteractor.preparePlayer(previewUrl)
        }
    }
    fun startPlayer() {
        viewModelScope.launch {
            playerInteractor.startPlayer()
            _currentPosition.postValue(playerInteractor.getCurrentPosition())
        }
    }

    fun pausePlayer() {
        viewModelScope.launch {
            playerInteractor.pausePlayer()
        }
    }

    private fun startUpdatingPosition() {
        updatePositionJob?.cancel()
        updatePositionJob = viewModelScope.launch {
            while (_playerState.value is PlayerStateStatus.STATE_PLAYING) {
                _currentPosition.postValue(playerInteractor.getCurrentPosition())
                delay(WAITING_TIME)
            }
        }
    }
    suspend fun isTrackFavorite(trackId: Int): Boolean {
        return favoriteInteractor.isFavorite(trackId)
    }

    private fun stopUpdatingPosition() {
        updatePositionJob?.cancel()
    }
    override fun onCleared() {
        viewModelScope.launch {
            playerInteractor.releasePlayer()
        }
        super.onCleared()
    }
}