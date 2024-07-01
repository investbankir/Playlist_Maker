package com.example.playlistmaker.sharing.domain.api

import android.content.Intent

interface SharingRepository {
    fun getShareIntent(shareText: String): Intent
    fun getSupportEmailIntent(email: String, subject: String, text: String): Intent
    fun getUserAgreementIntent(agreementUrl: String): Intent
}