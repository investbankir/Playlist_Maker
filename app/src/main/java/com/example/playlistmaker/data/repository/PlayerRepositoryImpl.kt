package com.example.playlistmaker.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.domain.models.PlayerStateStatus
import com.example.playlistmaker.domain.repository.PlayerRepository

class PlayerRepositoryImpl : PlayerRepository {

    private var mediaPlayer = MediaPlayer()
    private var playerState = PlayerStateStatus.STATE_DEFAULT
    private var audioPlayerListener : ((PlayerStateStatus) -> Unit)? = null

    override fun preparePlayer(url: String?){
        mediaPlayer.apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                playerState = PlayerStateStatus.STATE_PREPARED
                audioPlayerListener?.invoke(PlayerStateStatus.STATE_PREPARED)
            }
        }
    }
    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerStateStatus.STATE_PLAYING
        audioPlayerListener?.invoke(PlayerStateStatus.STATE_PLAYING)
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerStateStatus.STATE_PAUSED
        audioPlayerListener?.invoke(PlayerStateStatus.STATE_PAUSED)
    }

    override fun releasePlayer() {
        mediaPlayer.release()
        playerState = PlayerStateStatus.STATE_DEFAULT
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun setOnChangePlayerListener(listener: (PlayerStateStatus) -> Unit) {
        audioPlayerListener = listener
    }
}