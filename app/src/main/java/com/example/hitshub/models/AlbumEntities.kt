package com.example.hitshub.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
data class AlbumById(
    @Json(name = "id")
    override val id: Long,
    @Json(name = "title")
    override val title: String,
    @Json(name = "cover")
    override val cover_url: String,
    override val artist: Artist,
    @Json(name = "tracks")
    override val albumTracks: AlbumTracks
) : IAlbum

data class AlbumTracks(
    @Json(name = "data")
    val data: List<AlbumTrack>
)

@Parcelize
data class AlbumTrack(
    @Json(name = "id")
    override var id: Long,
    @Json(name = "title")
    override var title: String,
    @Json(name = "preview")
    override var preview: String
) : ITrack
