package com.example.playlistmaker.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.playlistmaker.media_library.ui.FavoritesViewModel
import com.example.playlistmaker.media_library.ui.PlaylistViewModel

val mediaLibraryModule = module {
    viewModel { FavoritesViewModel() }

    viewModel { PlaylistViewModel() }
}