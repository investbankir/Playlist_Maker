package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.example.playlistmaker.di.playerModule
import com.example.playlistmaker.di.searchModule
import com.example.playlistmaker.di.settingsModule
import com.example.playlistmaker.di.sharingModule


class App : Application() {
    companion object{
        const val LOG_SWITCHER = "LOG_SWITCHER"
        const val KEY_SWITCHER_THEME = "KEY_SWITCHER_THEME"
        lateinit var sharedPrefs : SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(playerModule, searchModule,settingsModule, sharingModule))
        }
        sharedPrefs = getSharedPreferences(LOG_SWITCHER, MODE_PRIVATE)
        val isDarkThemeFromPrefs = sharedPrefs.getBoolean(KEY_SWITCHER_THEME, false)

        applySavedTheme()
    }

    private fun applySavedTheme() {
        val isDarkThemeFromPrefs = sharedPrefs.getBoolean(KEY_SWITCHER_THEME, false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkThemeFromPrefs) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}