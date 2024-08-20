package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {
    private val iTunesUrl = "https://itunes.apple.com"

    val retrofit = Retrofit.Builder()
        .baseUrl(iTunesUrl)
        .addConverterFactory(
            GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(TrackApiService::class.java)

    override suspend fun doRequest(dto: Any): Response {
        return if (dto is TrackSearchRequest) {
            try {
                val response = iTunesService.search(dto.expression)
                response.apply { resultCode = 200 }
            } catch (e: Exception) {
                Response().apply { resultCode = 500 }
            }
        } else{
            Response().apply { resultCode = 400 }
        }
    }
}