package com.example.playlistmaker.di

import org.koin.dsl.module
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.api.ExternalNavigator
import org.koin.android.ext.koin.androidContext

val sharingModule = module {
    single<ExternalNavigator> { ExternalNavigatorImpl(androidContext()) }

    single<SharingInteractor> { SharingInteractorImpl(get()) }
}