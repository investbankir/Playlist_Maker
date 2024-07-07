package com.example.playlistmaker.di

import org.koin.dsl.module
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import  com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.data.repository.PlayerRepositoryImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import com.example.playlistmaker.player.ui.PlayerViewModel

val playerModule = module {

    single<PlayerRepository> { PlayerRepositoryImpl() }

    single<PlayerInteractor> { PlayerInteractorImpl(get()) }

    viewModel{ (previewUrl: String?) -> PlayerViewModel(get(), previewUrl) }
    }
