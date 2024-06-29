package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator


class App : Application() {
    companion object{
        const val LOG_SWITCHER = "LOG_SWITCHER"
        const val KEY_SWITCHER_THEME = "KEY_SWITCHER_THEME"
        lateinit var sharedPrefs : SharedPreferences
    }
    //var isDarkThemeFromPrefs = false
    override fun onCreate() {
        super.onCreate()

        sharedPrefs = getSharedPreferences(LOG_SWITCHER, MODE_PRIVATE)
        val isDarkThemeFromPrefs = sharedPrefs.getBoolean(KEY_SWITCHER_THEME, false)

        Creator.initialize(sharedPrefs)

        if (isDarkThemeFromPrefs) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}