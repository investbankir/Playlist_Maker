package com.example.playlistmaker

import android.os.Parcel
import android.os.Parcelable
import android.provider.ContactsContract.Data
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Track (
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
)