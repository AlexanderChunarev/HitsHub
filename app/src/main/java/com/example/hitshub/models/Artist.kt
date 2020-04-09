package com.example.hitshub.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Artist(
    @Json(name = "id")
    val id: Long,
    @Json(name = "name")
    val name: String,
    @Json(name = "picture")
    val picture: String,
    @Json(name = "picture_small")
    val pictureSmall: String,
    @Json(name = "picture_big")
    val pictureBig: String
) : Serializable
