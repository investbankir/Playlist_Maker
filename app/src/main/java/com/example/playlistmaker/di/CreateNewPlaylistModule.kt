package com.example.playlistmaker.di

import androidx.room.Room
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.playlistmaker.createNewPlaylist.ui.CreateNewPlaylistViewModel
import com.example.playlistmaker.createNewPlaylist.ui.PlaylistEditorViewModel
import com.example.playlistmaker.createNewPlaylist.data.PlaylistDbConvertor
import com.example.playlistmaker.createNewPlaylist.domain.db.CreateNewPlaylistRepository
import com.example.playlistmaker.createNewPlaylist.data.db.CreateNewPlaylistRepositoryImpl
import com.example.playlistmaker.createNewPlaylist.domain.db.CreateNewPlaylistInteractor
import com.example.playlistmaker.createNewPlaylist.domain.impl.CreateNewPlaylistInteractorImpl
import com.example.playlistmaker.media_library.data.db.AppDatabase
import org.koin.android.ext.koin.androidContext

val createNewPlaylistModule = module {
    viewModel{ CreateNewPlaylistViewModel(get())}
    viewModel{ PlaylistEditorViewModel(get())}

    single { Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
        .fallbackToDestructiveMigration()
        .build()}
    single { get<AppDatabase>().playlistDao() } //Ранее стоял TrackDao()

    factory { PlaylistDbConvertor(get()) }

    single<CreateNewPlaylistRepository> {
        CreateNewPlaylistRepositoryImpl(get(), get())
    }
    single<CreateNewPlaylistInteractor> {
        CreateNewPlaylistInteractorImpl(get(),get())
    }
}