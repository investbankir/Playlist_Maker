package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.PlayerStateStatus

interface PlayerRepository {
    fun preparePlayer(url: String?)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPosition(): Int
    fun setOnChangePlayerListener(listener: (PlayerStateStatus) -> Unit)
}