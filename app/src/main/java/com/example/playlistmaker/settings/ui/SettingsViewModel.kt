package com.example.playlistmaker.settings.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.sharing.domain.api.SharingInteractor


class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val _settingsState = MutableLiveData<SettingsState>()
    val settingsState: LiveData<SettingsState> get() = _settingsState
    init {
        synchronizeTheme()
    }
    fun synchronizeTheme() {
        val isDarkThemeEnabled = settingsInteractor.isDarkThemeEnabled()
        _settingsState.value = SettingsState(
            isDarkThemeEnabled = isDarkThemeEnabled
        )
        applyTheme(isDarkThemeEnabled)
    }

    fun setDarkTheme(enabled: Boolean) {
        settingsInteractor.setDarkTheme(enabled)
        _settingsState.value = _settingsState.value?.copy(isDarkThemeEnabled = enabled)
        applyTheme(enabled)
    }

    private fun applyTheme(enabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (enabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun shareApp(shareText: String) {
        sharingInteractor.shareText(shareText)
    }

    fun contactSupport(email: String, subject: String, text: String) {
        sharingInteractor.sendSupportEmail(email, subject, text)
    }

    fun openUserAgreement(url: String) {
        sharingInteractor.openUserAgreement(url)
    }
}
