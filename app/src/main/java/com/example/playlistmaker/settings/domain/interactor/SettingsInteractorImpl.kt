package com.example.playlistmaker.settings.domain.interactor

import android.content.SharedPreferences

class SettingsInteractorImpl(private val sharedPreferences: SharedPreferences) :
    SettingsInteractor {

    companion object {
        const val KEY_SWITCHER_THEME = "key_for_switcher"
    }
        override fun isDarkThemeEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_SWITCHER_THEME, false)
    }

    override fun setDarkTheme(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_SWITCHER_THEME, enabled)
            .apply()
    }
}