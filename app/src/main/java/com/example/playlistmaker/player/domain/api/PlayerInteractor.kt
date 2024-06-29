package com.example.playlistmaker.player.domain.api


import com.example.playlistmaker.player.ui.PlayerStateStatus


interface PlayerInteractor {
    fun preparePlayer(url: String?)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPosition(): Int
    fun setOnChangePlayerListener(listener: (PlayerStateStatus) -> Unit)
}