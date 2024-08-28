package com.example.playlistmaker.di

import android.media.MediaPlayer
import org.koin.dsl.module
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import  com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.data.repository.PlayerRepositoryImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import com.example.playlistmaker.player.ui.PlayerViewModel

val playerModule = module {
    factory { MediaPlayer() }

    factory <PlayerRepository> { PlayerRepositoryImpl(get()) }

    factory<PlayerInteractor> { PlayerInteractorImpl(get()) }

    viewModel{ (previewUrl: String?) -> PlayerViewModel(get(), get(), previewUrl) }
    }
