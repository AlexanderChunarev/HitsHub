package com.example.hitshub.models.chart.album

import com.squareup.moshi.Json

data class AlbumChartData(
    @Json(name = "data")
    val data: List<Albums>
)
