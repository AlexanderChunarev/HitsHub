package com.example.hitshub.models.album

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Album(
    @Json(name = "id")
    val id: Long,
    @Json(name = "title")
    val title: String,
    @Json(name = "cover")
    val cover_url: String,
    val artist: Artist,
    @Json(name = "tracks")
    val albumTracks: AlbumTracks
)
