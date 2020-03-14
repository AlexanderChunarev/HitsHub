package com.example.hitshub.models.chart.track

import com.example.hitshub.models.album.Artist
import com.squareup.moshi.Json

data class Track(
    @Json(name = "id")
    val id: Long,
    @Json(name = "title")
    val title: String,
    @Json(name = "preview")
    val preview: String,
    @Json(name = "position")
    val position: Int,
    val artist: Artist
)
