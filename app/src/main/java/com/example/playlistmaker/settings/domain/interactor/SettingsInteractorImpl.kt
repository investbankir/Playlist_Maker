package com.example.playlistmaker.settings.domain.interactor

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