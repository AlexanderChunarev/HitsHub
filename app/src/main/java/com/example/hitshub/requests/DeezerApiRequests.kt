package com.example.hitshub.requests

import com.example.hitshub.models.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeezerApiRequests {

    @GET("/search/album")
    fun searchAlbumByName(@Query("q") name: String): Call<SearchAlbumData>

    @GET("/search/track")
    fun searchTrackByName(@Query("q") name: String): Call<TrackData>

    @GET("/chart/0/tracks")
    fun getTrackByChart(): Call<ChartTracksData>

    @GET("/chart/0/albums")
    fun getAlbumByChart(): Call<ChartAlbumData>

    @GET("/album/{id}")
    fun getAlbumDataById(@Path("id")id: Long): Call<AlbumById>

    @GET("/track/{id}")
    fun getTrackById(@Path("id")id: Long): Call<Track>
}
