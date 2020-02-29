package com.example.hitshub.requests

import com.example.hitshub.models.Album
import com.example.hitshub.models.Track
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DeezerApiRequests {

    @GET("album?q={id}")
    fun getAlbumById(@Path("id") id: String): Call<Album>

    @GET("track?q={id}")
    fun getTrackById(@Path("id") id: String): Call<Track>
}
