package com.example.playlistmaker.player.domain.api


import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.models.PlayerStateStatus


interface
PlayerInteractor {
    suspend fun preparePlayer(url: String?): MediaPlayer
    suspend fun startPlayer(): Unit?
    suspend fun pausePlayer(): Unit?
    suspend fun releasePlayer()
    suspend fun getCurrentPosition(): Int
    fun setOnChangePlayerListener(listener: (PlayerStateStatus) -> Unit)
}