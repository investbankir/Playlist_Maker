package com.example.playlistmaker.player.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.models.PlayerStateStatus
import com.example.playlistmaker.player.domain.repositoty.PlayerRepository

class PlayerViewModel(
    private val playerRepository: PlayerRepository
) : ViewModel() {
    private val _playerState = MutableLiveData<PlayerStateStatus>().apply {
        value = PlayerStateStatus.STATE_DEFAULT
    }
    val playerState : LiveData<PlayerStateStatus> get() = _playerState

    private val _currentPosition = MutableLiveData<Int>().apply {
        value = 0
    }
    val currentPosition : LiveData<Int> get() = _currentPosition
    init {
        playerRepository.setOnChangePlayerListener { state ->
            _playerState.value = state
        }
    }
    fun preparePlayer(url: String?) {
        playerRepository.preparePlayer(url)
    }

    fun startPlayer() {
        playerRepository.startPlayer()
        updatePosition()
    }

    fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    fun releasePlayer() {
        playerRepository.releasePlayer()
    }

    fun updatePosition() {
        _currentPosition.value = playerRepository.getCurrentPosition()
    }
}