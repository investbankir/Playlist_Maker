package com.example.playlistmaker.sharing.domain.api

interface SharingInteractor {
    fun shareText(shareText: String)
    fun sendSupportEmail(email: String, subject: String, text: String)
    fun openUserAgreement(agreementUrl: String)
}