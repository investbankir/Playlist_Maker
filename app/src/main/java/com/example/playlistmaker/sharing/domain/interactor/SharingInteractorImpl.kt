package com.example.playlistmaker.sharing.domain.interactor

import android.content.Intent
import android.net.Uri
//import com.example.playlistmaker.sharing.data.ResourcesProvider
import com.example.playlistmaker.sharing.domain.api.SharingRepository


class SharingInteractorImpl(private val sharingRepository: SharingRepository) : SharingInteractor {
    override fun getShareIntent(shareText: String): Intent {
        return sharingRepository.getShareIntent(shareText)
    }

    override fun getSupportEmailIntent(email: String, subject: String, text: String): Intent {
        return sharingRepository.getSupportEmailIntent(email, subject, text)
    }

    override fun getUserAgreementIntent(agreementUrl: String): Intent {
        return sharingRepository.getUserAgreementIntent(agreementUrl)
    }
}