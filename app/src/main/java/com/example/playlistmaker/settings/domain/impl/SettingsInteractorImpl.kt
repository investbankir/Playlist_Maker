package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.api.SettingsRepository



class SettingsInteractorImpl(private val repository: SettingsRepository) :
    SettingsInteractor {

        override fun isDarkThemeEnabled(): Boolean {
        return repository.isDarkThemeEnabled()
    }

    override fun setDarkTheme(enabled: Boolean) {
        repository.setDarkTheme(enabled)
    }
}