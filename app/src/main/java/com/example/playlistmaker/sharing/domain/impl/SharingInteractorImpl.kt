package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.api.*


class SharingInteractorImpl(private val externalNavigator: ExternalNavigator) : SharingInteractor {
    override fun shareText(shareText: String) {
        externalNavigator.shareText(shareText)
    }

    override fun sendSupportEmail(email: String, subject: String, text: String) {
        externalNavigator.sendSupportEmail(email, subject, text)
    }

    override fun openUserAgreement(agreementUrl: String) {
        externalNavigator.openUserAgreement(agreementUrl)
    }
}