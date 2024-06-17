package com.example.playlistmaker.settings.domain.interactor

interface SettingsInteractor {
    fun isDarkThemeEnabled(): Boolean
    fun setDarkTheme(enabled: Boolean)
}