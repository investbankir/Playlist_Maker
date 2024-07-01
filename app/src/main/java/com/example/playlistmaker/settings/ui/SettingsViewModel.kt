package com.example.playlistmaker.settings.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.api.EmailData
import com.example.playlistmaker.sharing.domain.api.ShareData
import com.example.playlistmaker.sharing.domain.api.UrlData


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

    fun getShareData(shareText: String): ShareData {
        return sharingInteractor.getShareData(shareText)
    }

    fun getSupportEmailData(email: String, subject: String, text: String): EmailData {
        return sharingInteractor.getSupportEmailData(email, subject, text)
    }

    fun getUserAgreementData(url: String): UrlData {
        return sharingInteractor.getUserAgreementData(url)
    }
}
