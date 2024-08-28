package com.example.playlistmaker.search.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val trackId: Int,
    val artworkUrl100: String?,
    val trackName: String?,
    val artistName: String?,
    val collectionName: String?,
    val releaseDate : String?,
    val primaryGenreName: String?,
    val country : String?,
    val trackTimeMillis: Long?,
    val previewUrl : String?,
    var isFavorite: Boolean = false
) : Parcelable{
    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg")
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Track) return false

        if (trackId != other.trackId) return false

        return true
    }

    override fun hashCode(): Int {
        return trackId ?: 0
    }
}
