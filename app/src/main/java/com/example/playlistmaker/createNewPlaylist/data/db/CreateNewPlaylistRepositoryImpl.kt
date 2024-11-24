package com.example.playlistmaker.createNewPlaylist.data.db

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.createNewPlaylist.data.db.entity.PlaylistsEntity
import com.example.playlistmaker.media_library.data.db.AppDatabase
import com.example.playlistmaker.createNewPlaylist.domain.db.CreateNewPlaylistRepository
import com.example.playlistmaker.player.data.db.entity.TracksFromThePlaylistEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class CreateNewPlaylistRepositoryImpl(
    private val context: Context,
    private val appDatabase: AppDatabase): CreateNewPlaylistRepository {
    override fun addNewPlaylist(playlist: PlaylistsEntity): Flow<Long> = flow {
        val id = appDatabase.playlistDao().insertPlaylist(playlist)
        emit(id)
    }

    override fun updatePlaylist(playlist: PlaylistsEntity): Flow<Int> = flow {
        val updId = appDatabase.playlistDao().updatePlaylist(playlist)
        emit(updId)
    }

        override fun saveCoverPlaylist(uri: String): String {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"myAlbum")
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        val zonedDateTime = ZonedDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yy-MM-dd_HH-mm-ss")
        val fileName = formatter.format(zonedDateTime) + ".jpg"
        val file = File(filePath, fileName)

        val inputStream = context.contentResolver.openInputStream(uri.toUri())
        val outStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outStream)
        return file.toUri().toString()
    }

    override fun addTrackInPlaylist(track: TracksFromThePlaylistEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.tracksFromThePlaylistDao().insertTrackFromPlaylist(track)
        }
    }

    override fun deletePlaylistById(playlistId: Long): Flow<Long> = flow{
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.playlistDao().deletePlaylistById(playlistId)
        }
    }
}