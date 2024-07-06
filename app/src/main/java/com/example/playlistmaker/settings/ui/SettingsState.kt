package com.example.playlistmaker.settings.ui

data class SettingsState(
    val isDarkThemeEnabled: Boolean = false,
    val shareText: String = "",
    val supportEmail: String = "",
    val userAgreementUrl: String = ""
)