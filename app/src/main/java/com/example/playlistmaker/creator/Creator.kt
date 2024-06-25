package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.data.network.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.api.HistoryRepository
import com.example.playlistmaker.search.data.repository.HistoryRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.example.playlistmaker.search.domain.impl.HistoryInteractorImpl

object Creator {
    private fun getTrackRepository() : TracksRepository{
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    private fun getHistoryRepository() : HistoryRepository{
        return HistoryRepositoryImpl()
    }
    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(getTrackRepository())
    }

    fun provideHistoryInteractor(): HistoryInteractor {
        return HistoryInteractorImpl(getHistoryRepository())
    }
}