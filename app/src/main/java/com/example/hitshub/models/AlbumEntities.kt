package com.example.hitshub.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Album(
    @Json(name = "id")
    val id: Long,
    @Json(name = "title")
    val title: String,
    @Json(name = "cover")
    val cover_url: String,
    val artist: Artist,
    @Json(name = "tracks")
    val albumTracks: AlbumTracks
) : IAlbum

data class AlbumTracks(
    @Json(name = "data")
    val data: List<AlbumTrack>
)

data class AlbumTrack(
    @Json(name = "id")
    override val id: Long,
    @Json(name = "title")
    override val title: String,
    @Json(name = "preview")
    override val preview: String
) : ITrack
