package com.example.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.settings.domain.interactor.SettingsInteractor
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor


class SettingsViewModelFactory(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(settingsInteractor, sharingInteractor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}