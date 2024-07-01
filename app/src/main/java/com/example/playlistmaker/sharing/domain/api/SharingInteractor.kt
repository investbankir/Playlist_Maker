package com.example.playlistmaker.sharing.domain.api

interface SharingInteractor {
    fun getShareData(shareText: String): ShareData
    fun getSupportEmailData(email: String, subject: String, text: String): EmailData
    fun getUserAgreementData(agreementUrl: String): UrlData
}