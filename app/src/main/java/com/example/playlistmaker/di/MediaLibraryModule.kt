package com.example.playlistmaker.di

import androidx.room.Room
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.playlistmaker.media_library.ui.favorites.FavoritesViewModel
import com.example.playlistmaker.media_library.ui.playlist.PlaylistViewModel
import com.example.playlistmaker.media_library.data.db.AppDatabase
import com.example.playlistmaker.media_library.data.TrackDbConvertor
import com.example.playlistmaker.media_library.domain.db.FavoriteRepository
import com.example.playlistmaker.media_library.data.FavoriteRepositoryImpl
import  com.example.playlistmaker.media_library.domain.db.FavoriteInteractor
import  com.example.playlistmaker.media_library.domain.impl.FavoriteInteractorImpl
import com.example.playlistmaker.media_library.domain.db.PlaylistsRepository
import com.example.playlistmaker.media_library.data.db.PlaylistsRepositoryImpl
import com.example.playlistmaker.media_library.domain.db.PlaylistsInteractor
import com.example.playlistmaker.media_library.domain.impl.PlaylistsInteractorImpl
import com.example.playlistmaker.player.data.TracksFromThePlaylistDbConverter



import org.koin.android.ext.koin.androidContext

val mediaLibraryModule = module {
    viewModel { FavoritesViewModel(get()) }

    viewModel { PlaylistViewModel(get()) }

    single { Room.databaseBuilder(androidContext(),AppDatabase::class.java, "database.db")
        .fallbackToDestructiveMigration()
        .build()}
    single { get<AppDatabase>().trackDao() }

    factory { TrackDbConvertor() }
    factory { TracksFromThePlaylistDbConverter() }



    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(),get())
    }
    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get(),get())
    }
    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(get(), get(), get())
    }
    single<PlaylistsInteractor> {
        PlaylistsInteractorImpl(get())
    }
}