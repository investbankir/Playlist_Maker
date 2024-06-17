package com.example.playlistmaker.settings.ui
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.domain.interactor.SettingsInteractorImpl
import com.example.playlistmaker.settings.presentation.SettingsViewModel
import com.example.playlistmaker.settings.presentation.SettingsViewModelFactory
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractorImpl
import com.example.playlistmaker.sharing.presentation.SharingViewModel
import com.example.playlistmaker.sharing.presentation.SharingViewModelFactory
import com.example.playlistmaker.sharing.data.ResourcesProvider
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(SettingsInteractorImpl(getSharedPreferences("settings", MODE_PRIVATE)))
    }


    private val sharingViewModel: SharingViewModel by viewModels {
        SharingViewModelFactory(SharingInteractorImpl(ResourcesProvider(this)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backMainActivity = findViewById<Button>(R.id.backMainActivity)
        backMainActivity.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val themeSwitch = findViewById<SwitchMaterial>(R.id.themeSwitch)

        settingsViewModel.darkThemeEnabled.observe(this) { isEnabled ->
            themeSwitch.isChecked = isEnabled
        }

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.setDarkTheme(isChecked)
        }

        val toShare = findViewById<Button>(R.id.toShare)
        toShare.setOnClickListener {
            startActivity(sharingViewModel.getShareIntent(getString(R.string.shareApp)))
        }

        val messageToSupport = findViewById<Button>(R.id.massageToSupport)
        messageToSupport.setOnClickListener {
            startActivity(
                sharingViewModel.getSupportEmailIntent(
                    getString(R.string.emailDeveloper),
                    getString(R.string.titleMassageSupport),
                    getString(R.string.textMassageSupport)
                )
            )
        }

        val sendUserAgreement = findViewById<Button>(R.id.sendUserAgreement)
        sendUserAgreement.setOnClickListener {
            startActivity(sharingViewModel.getUserAgreementIntent(getString(R.string.offer)))
        }
    }
}