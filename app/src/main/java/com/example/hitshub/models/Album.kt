package com.example.hitshub.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Album(
    @Json(name = "id")
    val id: Long,
    @Json(name = "title")
    val title: String,
    @Json(name = "tracks")
    val data: AlbumData,
    @Json(name = "picture")
    val picture_album_url: String
)
