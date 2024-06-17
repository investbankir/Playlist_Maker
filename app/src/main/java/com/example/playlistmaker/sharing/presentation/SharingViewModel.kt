package com.example.playlistmaker.sharing.presentation

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor

class SharingViewModel(sharingInteractor: SharingInteractor) : ViewModel() {

    fun getShareIntent(shareText: String): Intent {
        return Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
    }

    fun getSupportEmailIntent(email: String, subject: String, text : String) : Intent{
        return Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:")).apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }
    }

    fun getUserAgreementIntent(url : String) : Intent{
        return Intent(Intent.ACTION_VIEW, Uri.parse(url))
    }
}