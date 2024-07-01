package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.api.*


class SharingInteractorImpl(private val sharingRepository: SharingRepository) : SharingInteractor {
    override fun getShareData(shareText: String): ShareData {
        return ShareData(shareText)
    }

    override fun getSupportEmailData(email: String, subject: String, text: String): EmailData {
        return EmailData(email, subject, text)
    }

    override fun getUserAgreementData(agreementUrl: String): UrlData {
        return UrlData(agreementUrl)
    }
}