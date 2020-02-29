package com.example.hitshub.models

import com.squareup.moshi.Json

data class AlbumData(
    @Json(name = "data")
    val tracks: List<Track>
)