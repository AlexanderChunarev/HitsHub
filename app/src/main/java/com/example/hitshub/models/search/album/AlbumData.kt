package com.example.hitshub.models.search.album

import com.example.hitshub.models.chart.album.Albums
import com.squareup.moshi.Json

data class AlbumData(
    @Json(name = "data")
    val data: List<Albums>
)
