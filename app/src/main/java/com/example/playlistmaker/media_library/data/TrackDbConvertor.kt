package com.example.playlistmaker.media_library.data

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.media_library.data.db.entity.TrackEntity

class TrackDbConvertor {
    fun map(track: Track): TrackEntity{
        return TrackEntity(
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

    fun map(track: TrackEntity) : Track{
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
            track.previewUrl,
        )
    }
}