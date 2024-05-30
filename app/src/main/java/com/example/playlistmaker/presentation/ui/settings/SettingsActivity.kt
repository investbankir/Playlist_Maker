package com.example.playlistmaker.presentation.ui.settings
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.presentation.ui.App
import com.example.playlistmaker.R
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    companion object {
        private const val LOG_SWITCHER = "log_switcher"
        private const val KEY_SWITCHER_THEME = "key_for_switcher"
        private var sharedPrefsSwitch = App.sharedPrefs
    }
    var darkTheme = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backMainActivity = findViewById<Button>(R.id.backMainActivity)
        backMainActivity.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val themeSwitch = findViewById<SwitchMaterial>(R.id.themeSwitch)
        sharedPrefsSwitch = getSharedPreferences(LOG_SWITCHER, MODE_PRIVATE)
        themeSwitch.isChecked = sharedPrefsSwitch.getBoolean(
            KEY_SWITCHER_THEME, resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK==Configuration.UI_MODE_NIGHT_YES)
        switcherDarkTheme(themeSwitch.isChecked)
        themeSwitch.setOnCheckedChangeListener{buttonView, isChecked ->
            sharedPrefsSwitch.edit().putBoolean(KEY_SWITCHER_THEME, isChecked).apply()
            switcherDarkTheme(isChecked)
        }

        val toShare = findViewById<Button>(R.id.toShare)
        toShare.setOnClickListener {
            val shareText = getString(R.string.shareApp)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
            startActivity(shareIntent)
        }
        val massageToSupport = findViewById<Button>(R.id.massageToSupport)
        massageToSupport.setOnClickListener{
            val title = getString(R.string.titleMassageSupport)
            val text = getString(R.string.textMassageSupport)
            val massageIntent = Intent(Intent.ACTION_SENDTO)
            val email = getString(R.string.emailDeveloper)
            massageIntent.data = Uri.parse("mailto:")
            massageIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            massageIntent.putExtra(Intent.EXTRA_SUBJECT, title)
            massageIntent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(massageIntent)
        }
        val sendUserAgreement = findViewById<Button>(R.id.sendUserAgreement)
        sendUserAgreement.setOnClickListener{
            val offer = getString(R.string.offer)

            val offerIntent = Intent(Intent.ACTION_VIEW)
            offerIntent.data = Uri.parse(offer)
            startActivity(offerIntent)
        }
    }

    fun switcherDarkTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        App.sharedPrefs.edit()
            .putBoolean(App.KEY_SWITCHER_THEME, darkTheme)
            .apply()
    }
}