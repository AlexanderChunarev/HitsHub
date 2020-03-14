package com.example.hitshub.models.chart.album

import com.example.hitshub.models.album.Artist
import com.squareup.moshi.Json

data class Albums(
    @Json(name = "id")
    val id: Long,
    @Json(name = "title")
    val title: String,
    @Json(name = "cover")
    val cover_url: String,
    val artist: Artist,
    @Json(name = "tracklist")
    val tracklist_url: String
)
