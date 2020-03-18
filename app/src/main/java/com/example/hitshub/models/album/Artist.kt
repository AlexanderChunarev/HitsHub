package com.example.hitshub.models.album

import com.squareup.moshi.Json

data class Artist(
    @Json(name = "id")
    val id: Long,
    @Json(name = "name")
    val name: String,
    @Json(name = "picture")
    val picture_url: String
)
