package com.example.playlistmaker.search.data.dto

import android.os.Parcelable
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackDto(
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
) : Parcelable {
    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg")
}
