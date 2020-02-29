package com.example.hitshub.models

import com.squareup.moshi.Json

data class Track(
    @Json(name = "id")
    val id: Long,
    @Json(name = "title")
    val title: String,
    @Json(name = "preview")
    val url: String,
    @Json(name = "cover_small")
    val small_image_url: String,
    @Json(name = "cover_big")
    val big_image_url: String
)
