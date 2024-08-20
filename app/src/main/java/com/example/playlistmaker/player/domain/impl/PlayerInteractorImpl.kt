package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.models.PlayerStateStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {
    override suspend fun preparePlayer(url: String?) = withContext(Dispatchers.IO) {
        playerRepository.preparePlayer(url)
    }

    override suspend fun startPlayer() = withContext(Dispatchers.IO) {
        playerRepository.startPlayer()
    }

    override suspend fun pausePlayer() = withContext(Dispatchers.IO) {
        playerRepository.pausePlayer()
    }

    override suspend fun releasePlayer() = withContext(Dispatchers.IO) {
        playerRepository.releasePlayer()
    }

    override suspend fun getCurrentPosition(): Int = withContext(Dispatchers.IO) {
        playerRepository.getCurrentPosition()
    }

    override fun setOnChangePlayerListener(listener: (PlayerStateStatus) -> Unit) {
        playerRepository.setOnChangePlayerListener(listener)
    }
}