package com.example.playlistmaker.player.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.player.domain.api.PlayerInteractor

class PlayerViewModelFactory(
    private val playerInteractor: PlayerInteractor,
    private val previewUrl: String?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayerViewModel(playerInteractor, previewUrl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}