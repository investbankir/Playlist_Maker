package com.example.playlistmaker.sharing.domain.interactor

import android.content.Intent

interface SharingInteractor {
    fun getShareIntent(shareText: String): Intent
    fun getSupportEmailIntent(email: String, subject: String, text: String): Intent
    fun getUserAgreementIntent(agreementUrl: String): Intent
}