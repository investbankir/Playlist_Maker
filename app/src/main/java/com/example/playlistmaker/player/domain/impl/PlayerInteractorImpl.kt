package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.models.PlayerStateStatus


class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {
    override fun preparePlayer(url: String?) {
        playerRepository.preparePlayer(url)
    }

    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun releasePlayer() {
        playerRepository.releasePlayer()
    }

    override fun getCurrentPosition(): Int {
        return playerRepository.getCurrentPosition()
    }

    override fun setOnChangePlayerListener(listener: (PlayerStateStatus) -> Unit) {
        playerRepository.setOnChangePlayerListener(listener)
    }
}