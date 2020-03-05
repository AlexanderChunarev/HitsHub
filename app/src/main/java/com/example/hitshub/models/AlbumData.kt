package com.example.hitshub.models

import com.squareup.moshi.Json

data class AlbumData(
    @Json(name = "title")
    val title: String,
    @Json(name="cover")
    val  cover_url:String,
    @Json(name="tracks")
    val tracks :TracksData
)
