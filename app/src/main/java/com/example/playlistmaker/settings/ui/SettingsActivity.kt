package com.example.playlistmaker.settings.ui
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.sharing.ui.ExternalNavigator
import com.google.android.material.switchmaterial.SwitchMaterial
class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var themeSwitch: SwitchMaterial
    private lateinit var externalNavigator: ExternalNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        themeSwitch = findViewById(R.id.themeSwitch)
        externalNavigator = Creator.provideExternalNavigator(this)

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
            val shareData = viewModel.getShareData(getString(R.string.shareApp))
            val shareIntent = externalNavigator.createShareIntent(shareData)
            startActivity(shareIntent)
        }

        val messageToSupport = findViewById<Button>(R.id.massageToSupport)
        messageToSupport.setOnClickListener {
            val emailData = viewModel.getSupportEmailData(
                getString(R.string.emailDeveloper),
                getString(R.string.titleMassageSupport),
                getString(R.string.textMassageSupport)
            )
            val emailIntent = externalNavigator.createSupportEmailIntent(emailData)
            startActivity(emailIntent)
        }

        val sendUserAgreement = findViewById<Button>(R.id.sendUserAgreement)
        sendUserAgreement.setOnClickListener {
            val urlData = viewModel.getUserAgreementData(getString(R.string.offer))
            val userAgreementIntent = externalNavigator.createUserAgreementIntent(urlData)
            startActivity(userAgreementIntent)
        }

        val backMainActivity = findViewById<Button>(R.id.backMainActivity)
        backMainActivity.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}