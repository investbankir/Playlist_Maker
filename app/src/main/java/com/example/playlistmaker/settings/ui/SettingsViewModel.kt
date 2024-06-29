package com.example.playlistmaker.settings.ui


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.drawable.DrawableCompat.applyTheme
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.ui.SearchState
import com.example.playlistmaker.settings.domain.interactor.SettingsInteractor
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor


class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val _settingsState = MutableLiveData<SettingsState>()
    val settingsState: LiveData<SettingsState> get() = _settingsState
    init {
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

    fun getShareIntent(shareText: String): Intent {
       // _settingsState.value = _settingsState.value?.copy(shareText = shareText)
        return sharingInteractor.getShareIntent(shareText)
    }

    fun getSupportEmailIntent(email: String, subject: String, text: String): Intent {
       // _settingsState.value = _settingsState.value?.copy(supportEmail = email)
        return sharingInteractor.getSupportEmailIntent(email, subject, text)
    }

    fun getUserAgreementIntent(url: String): Intent {
       // _settingsState.value = _settingsState.value?.copy(userAgreementUrl = url)
        return sharingInteractor.getUserAgreementIntent(url)
    }
}
