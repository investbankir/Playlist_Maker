package com.example.playlistmaker.creator

import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.data.network.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.impl.SearchInteractorImpl

object Creator {
    private fun getTrackRepository() : TracksRepository{
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    private fun getSearchInteractor() : SearchInteractor {
        return SearchInteractorImpl(getTrackRepository())
    }
}