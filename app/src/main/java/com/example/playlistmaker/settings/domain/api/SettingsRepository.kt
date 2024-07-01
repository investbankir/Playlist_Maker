package com.example.playlistmaker.settings.domain.api

interface SettingsRepository {
    fun isDarkThemeEnabled(): Boolean
    fun setDarkTheme(enabled: Boolean)
}