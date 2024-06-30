package com.example.playlistmaker.creator

import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.data.network.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.api.HistoryRepository
import com.example.playlistmaker.search.data.repository.HistoryRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.example.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import  com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.repository.PlayerRepository
import com.example.playlistmaker.player.data.repository.PlayerRepositoryImpl
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.api.SettingsRepository
import com.example.playlistmaker.settings.data.repository.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import android.content.SharedPreferences
import com.example.playlistmaker.sharing.domain.api.SharingRepository
import com.example.playlistmaker.sharing.data.repository.SharingRepositoryImpl
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var settingsInteractor: SettingsInteractor

    fun initialize(sharedPrefs: SharedPreferences) {
        settingsRepository = SettingsRepositoryImpl(sharedPrefs)
        settingsInteractor = SettingsInteractorImpl(settingsRepository)
    }
    private fun getTrackRepository() : TracksRepository{
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    private fun getHistoryRepository() : HistoryRepository{
        return HistoryRepositoryImpl()
    }
    private fun getPlayerRepository() : PlayerRepository{
        return PlayerRepositoryImpl()
    }
    private fun getSharingRepository() : SharingRepository {
        return SharingRepositoryImpl()
    }
    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(getTrackRepository())
    }

    fun provideHistoryInteractor(): HistoryInteractor {
        return HistoryInteractorImpl(getHistoryRepository())
    }
    fun providePlayerInteractor() : PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }
    fun provideSettingsInteractor() : SettingsInteractor {
        if (!this::settingsInteractor.isInitialized) {
            throw IllegalStateException("Creator is not initialized")
        }
        return settingsInteractor
    }
    fun provideSharingInteractor() : SharingInteractor {
        return SharingInteractorImpl(getSharingRepository())
    }
}