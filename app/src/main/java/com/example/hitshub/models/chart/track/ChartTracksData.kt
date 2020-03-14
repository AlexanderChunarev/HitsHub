package com.example.hitshub.models.chart.track

import com.squareup.moshi.Json

data class ChartTracksData(
    @Json(name = "data")
    val data: List<Track>
)
