package com.example.hitshub.models

import com.squareup.moshi.Json

data class ChartTracksData(
    @Json(name = "data")
    val data: List<ChartTrack>
)

data class ChartTrack(
    @Json(name = "id")
    val id: Long,
    @Json(name = "title")
    val title: String,
    @Json(name = "preview")
    val preview: String,
    @Json(name = "position")
    val position: Int,
    val artist: Artist
) : ITrack

data class AlbumChartData(
    @Json(name = "data")
    val data: List<ChartAlbum>
)

data class ChartAlbum(
    @Json(name = "id")
    val id: Long,
    @Json(name = "title")
    val title: String,
    @Json(name = "cover")
    val cover_url: String,
    val artist: Artist,
    @Json(name = "tracklist")
    val tracklist_url: String
) : IAlbum
