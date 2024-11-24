package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.media_library.data.db.AppDatabase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.playlistmaker.selected_playlist.ui.SelectedPlaylistViewModel
import com.example.playlistmaker.selected_playlist.domain.impl.SelectedPlaylistInteractorImpl
import com.example.playlistmaker.selected_playlist.domain.api.SelectedPlaylistInteractor
import org.koin.android.ext.koin.androidContext

val selectedPlaylistModule = module {
    viewModel { SelectedPlaylistViewModel (get(), get(), get()) }

    single { Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
        .fallbackToDestructiveMigration()
        .build()}

    single { get<AppDatabase>().playlistDao() }

    single<SelectedPlaylistInteractor> {
        SelectedPlaylistInteractorImpl(get())
    }
}