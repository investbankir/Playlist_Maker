package com.example.playlistmaker.sharing.ui
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.sharing.domain.api.EmailData
import com.example.playlistmaker.sharing.domain.api.ShareData
import com.example.playlistmaker.sharing.domain.api.UrlData

class ExternalNavigator(private val context: Context) {

    fun createShareIntent(data: ShareData): Intent {
        return Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, data.text)
        }
    }

    fun createSupportEmailIntent(emaildata: EmailData): Intent {
        return Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emaildata.email))
            putExtra(Intent.EXTRA_SUBJECT, emaildata.subject)
            putExtra(Intent.EXTRA_TEXT, emaildata.text)
        }
    }

    fun createUserAgreementIntent(urlData: UrlData): Intent {
        return Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(urlData.url)
        }
    }
}