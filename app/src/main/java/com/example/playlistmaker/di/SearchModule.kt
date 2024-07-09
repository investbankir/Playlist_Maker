package com.example.playlistmaker.di

import org.koin.dsl.module
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.data.network.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.api.HistoryRepository
import com.example.playlistmaker.search.data.repository.HistoryRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.example.playlistmaker.search.domain.impl.HistoryInteractorImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.search.data.NetworkClient
import com.google.gson.Gson

val searchModule = module {

    single { Gson() }
    single<NetworkClient>{RetrofitNetworkClient() }

    single<TracksRepository> { TracksRepositoryImpl(get()) }

    single<HistoryRepository> { HistoryRepositoryImpl(get(), get()) }

    factory<SearchInteractor> { SearchInteractorImpl(get()) }

    factory<HistoryInteractor> { HistoryInteractorImpl(get()) }

    viewModel { SearchViewModel(get(), get()) }

}