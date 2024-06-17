package com.example.playlistmaker.player.domain.repositoty

import com.example.playlistmaker.player.domain.models.PlayerStateStatus

interface PlayerRepository {
    fun preparePlayer(url: String?)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPosition(): Int
    fun setOnChangePlayerListener(listener: (PlayerStateStatus) -> Unit)
}