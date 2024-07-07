package com.example.playlistmaker.settings.ui
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModel<SettingsViewModel>()
    private lateinit var themeSwitch: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        themeSwitch = findViewById(R.id.themeSwitch)

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

        findViewById<Button>(R.id.toShare).setOnClickListener {
            viewModel.shareApp(getString(R.string.shareApp))
        }

        findViewById<Button>(R.id.massageToSupport).setOnClickListener {
            viewModel.contactSupport(
                getString(R.string.emailDeveloper),
                getString(R.string.titleMassageSupport),
                getString(R.string.textMassageSupport)
            )
        }

        findViewById<Button>(R.id.sendUserAgreement).setOnClickListener {
            viewModel.openUserAgreement(getString(R.string.offer))
        }


        val backMainActivity = findViewById<Button>(R.id.backMainActivity)
        backMainActivity.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}