package com.example.playlistmaker.player.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.models.PlayerStateStatus
import com.example.playlistmaker.player.domain.api.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : PlayerRepository {

    private var playerState: PlayerStateStatus = PlayerStateStatus.STATE_DEFAULT()
    private var audioPlayerListener: ((PlayerStateStatus) -> Unit)? = null

    override suspend fun preparePlayer(url: String?) = withContext(Dispatchers.IO) {
        mediaPlayer.reset()
        mediaPlayer.apply {
            setDataSource(url)
            setOnPreparedListener {
                playerState = PlayerStateStatus.STATE_PREPARED()
                audioPlayerListener?.invoke(playerState)
            }
            setOnCompletionListener {
                playerState = PlayerStateStatus.STATE_PREPARED()
                audioPlayerListener?.invoke(playerState)
            }
            prepareAsync()
        }
    }
    override suspend fun startPlayer() = withContext(Dispatchers.IO) {
        mediaPlayer.start()
        playerState = PlayerStateStatus.STATE_PLAYING(mediaPlayer.currentPosition.toTimeString())
        audioPlayerListener?.invoke(playerState)
    }

    override suspend fun pausePlayer() = withContext(Dispatchers.IO) {
        mediaPlayer.pause()
        playerState = PlayerStateStatus.STATE_PAUSED(mediaPlayer.currentPosition.toTimeString())
        audioPlayerListener?.invoke(playerState)
    }

    override suspend fun releasePlayer() = withContext(Dispatchers.IO) {
        mediaPlayer.release()
        playerState = PlayerStateStatus.STATE_DEFAULT()
    }

    override suspend fun getCurrentPosition(): Int = withContext(Dispatchers.IO)   {
        mediaPlayer.currentPosition
    }

    override fun setOnChangePlayerListener(listener: (PlayerStateStatus) -> Unit) {
        audioPlayerListener = listener
    }

    fun Int.toTimeString(): String {
        val minutes = (this / 1000) / 60
        val seconds = (this / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}