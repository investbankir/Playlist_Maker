package com.example.playlistmaker.di

import androidx.room.Room
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.playlistmaker.media_library.ui.FavoritesViewModel
import com.example.playlistmaker.media_library.ui.PlaylistViewModel
import com.example.playlistmaker.media_library.data.db.AppDatabase
import com.example.playlistmaker.media_library.data.TrackDbConvertor
import com.example.playlistmaker.media_library.domain.db.FavoriteRepository
import com.example.playlistmaker.media_library.data.FavoriteRepositoryImpl
import  com.example.playlistmaker.media_library.domain.db.FavoriteInteractor
import  com.example.playlistmaker.media_library.domain.impl.FavoriteInteractorImpl



import org.koin.android.ext.koin.androidContext

val mediaLibraryModule = module {
    viewModel { FavoritesViewModel(get()) }

    viewModel { PlaylistViewModel() }

    single { Room.databaseBuilder(androidContext(),AppDatabase::class.java, "database.db")
        .build()}

    factory { TrackDbConvertor() }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(),get())
    }

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get(),get())
    }
}