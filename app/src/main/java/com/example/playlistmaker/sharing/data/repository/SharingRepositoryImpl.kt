package com.example.playlistmaker.sharing.data.repository

import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.sharing.domain.api.SharingRepository


class SharingRepositoryImpl : SharingRepository {
    override fun getShareIntent(shareText: String): Intent {
        return Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
    }

    override fun getSupportEmailIntent(email: String, subject: String, text: String): Intent {
        return Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }
    }

    override fun getUserAgreementIntent(agreementUrl: String): Intent {
        return Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(agreementUrl)
        }
    }
}