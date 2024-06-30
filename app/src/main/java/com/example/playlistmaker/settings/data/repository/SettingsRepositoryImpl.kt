package com.example.playlistmaker.settings.data.repository

import com.example.playlistmaker.settings.domain.api.SettingsRepository
import com.example.playlistmaker.App
import android.content.SharedPreferences

class SettingsRepositoryImpl(private val sharedPrefs : SharedPreferences) : SettingsRepository {
    override fun isDarkThemeEnabled(): Boolean {
        return sharedPrefs.getBoolean("KEY_SWITCHER_THEME", false)
    }


    override fun setDarkTheme(enabled: Boolean) {
        sharedPrefs.edit()
            .putBoolean("KEY_SWITCHER_THEME", enabled)
            .apply()
    }
}