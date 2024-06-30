package com.example.playlistmaker.settings.ui
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.google.android.material.switchmaterial.SwitchMaterial
class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var themeSwitch: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        themeSwitch = findViewById(R.id.themeSwitch)

        val factory = SettingsViewModelFactory(
            Creator.provideSettingsInteractor(),
            Creator.provideSharingInteractor()
        )

        viewModel = ViewModelProvider(this, factory).get(SettingsViewModel::class.java)

        viewModel.settingsState.observe(this) { state ->
            handleSettingsState(state)
        }

        setupUi()
    }
    override fun onResume() {
        super.onResume()
        viewModel.synchronizeTheme()
    }

    private fun handleSettingsState(state: SettingsState) {
        themeSwitch.isChecked = state.isDarkThemeEnabled
    }

    private fun setupUi() {

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setDarkTheme(isChecked)
        }

        val toShare = findViewById<Button>(R.id.toShare)
        toShare.setOnClickListener {
            startActivity(viewModel.getShareIntent(getString(R.string.shareApp)))
        }

        val messageToSupport = findViewById<Button>(R.id.massageToSupport)
        messageToSupport.setOnClickListener {
            startActivity(
                viewModel.getSupportEmailIntent(
                    getString(R.string.emailDeveloper),
                    getString(R.string.titleMassageSupport),
                    getString(R.string.textMassageSupport)
                )
            )
        }

        val sendUserAgreement = findViewById<Button>(R.id.sendUserAgreement)
        sendUserAgreement.setOnClickListener {
            startActivity(viewModel.getUserAgreementIntent(getString(R.string.offer)))
        }

        val backMainActivity = findViewById<Button>(R.id.backMainActivity)
        backMainActivity.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}