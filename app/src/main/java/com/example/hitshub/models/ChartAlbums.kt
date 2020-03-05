package com.example.hitshub.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChartAlbums(
    @Json(name = "data")
    val data: List<ChartAlbumData>
)