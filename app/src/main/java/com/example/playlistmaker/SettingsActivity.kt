package com.example.playlistmaker
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.MainActivity
import com.example.playlistmaker.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backMainActivity = findViewById<Button>(R.id.backMainActivity)
        backMainActivity.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
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
            massageIntent.data = Uri.parse("mailto:")
            massageIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("leopardo4174@ya.ru"))
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
}