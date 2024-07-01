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

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val resp = iTunesService.search(dto.expression).execute()
            val body = resp.body() ?: Response()
            return body.apply { resultCode = resp.code()  }
        } else{
            return Response().apply { resultCode = 400 }
        }
    }
}