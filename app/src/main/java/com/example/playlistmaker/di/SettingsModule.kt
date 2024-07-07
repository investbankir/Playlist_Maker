package com.example.playlistmaker.di

import org.koin.dsl.module
import android.content.Context
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.api.SettingsRepository
import com.example.playlistmaker.settings.data.repository.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import android.content.SharedPreferences
import org.koin.androidx.viewmodel.dsl.viewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.android.ext.koin.androidContext

val settingsModule = module {
    single<SharedPreferences> {
        androidContext().getSharedPreferences("LOG_SWITCHER", Context.MODE_PRIVATE)
    }

    single<SettingsRepository> { SettingsRepositoryImpl(get()) }

    single<SettingsInteractor> { SettingsInteractorImpl(get()) }

    viewModel {SettingsViewModel(get(), get())}
}