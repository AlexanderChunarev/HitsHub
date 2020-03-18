package com.example.hitshub.models.search.track

import com.example.hitshub.models.track.Track
import com.squareup.moshi.Json

data class TrackData(
    @Json(name = "data")
    val data: List<Track>
)
