package com.example.hitshub.models.album

import com.squareup.moshi.Json

data class Track(
    @Json(name = "id")
    val id: Long,
    @Json(name = "title")
    val title: String,
    @Json(name = "preview")
    val preview: String
)
