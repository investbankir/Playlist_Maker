package com.example.playlistmaker.player.data

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.data.db.entity.TracksFromThePlaylistEntity

class TracksFromThePlaylistDbConverter {
    fun map(track: Track): TracksFromThePlaylistEntity {
        return TracksFromThePlaylistEntity(
            track.trackId,
            track.artworkUrl100,
            track.trackName,
            track.artistName,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.trackTimeMillis,
            track.previewUrl,
        )
    }

    fun map(track: TracksFromThePlaylistEntity): Track {
        return Track(
            track.trackId,
            track.artworkUrl100,
            track.trackName,
            track.artistName,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.trackTimeMillis,
            track.previewUrl
        )
    }
}
