package com.example.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.api.PlayerInteractor

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val previewUrl: String?
) : ViewModel() {
    companion object {
        private const val WAITING_TIME = 400L
    }
    private val _playerState = MutableLiveData<PlayerStateStatus>().apply {
        value = PlayerStateStatus.STATE_DEFAULT
    }
    val playerState : LiveData<PlayerStateStatus> get() = _playerState

    private val _currentPosition = MutableLiveData<Int>().apply {
        value = 0
    }
    val currentPosition : LiveData<Int> get() = _currentPosition

    private val handler = Handler(Looper.getMainLooper())
    private val updatePositionRunnable = object : Runnable {
        override fun run() {
            if (_playerState.value == PlayerStateStatus.STATE_PLAYING) {
                _currentPosition.postValue(playerInteractor.getCurrentPosition())
                handler.postDelayed(this, WAITING_TIME)
            }
        }
    }
    init {
        playerInteractor.setOnChangePlayerListener { state ->
            _playerState.value = state
            if (state == PlayerStateStatus.STATE_PLAYING) {
                handler.post(updatePositionRunnable)
            } else {
                handler.removeCallbacks(updatePositionRunnable)
            }
        }
        preparePlayer()
    }
    private fun preparePlayer() {
        playerInteractor.preparePlayer(previewUrl)
    }
    fun startPlayer() {
        playerInteractor.startPlayer()
        _currentPosition.value = playerInteractor.getCurrentPosition()
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
    }
    override fun onCleared() {
        playerInteractor.releasePlayer()
        super.onCleared()
    }
}