package com.example.hitshub.models

import com.squareup.moshi.Json

data class TrackData(
    @Json(name = "data")
    val data: List<Track>
)

data class AlbumData(
    @Json(name = "data")
    val data: List<ChartAlbum>
)
