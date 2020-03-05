package com.example.hitshub.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchAlbums(
    @Json(name = "data")
    val searchedAlbums: List<Album>
)
