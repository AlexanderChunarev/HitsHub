package com.example.hitshub.models.album

import com.squareup.moshi.Json

data class AlbumTracks(
    @Json(name = "data")
    val data: List<Track>
)
