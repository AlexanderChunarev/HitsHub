package com.example.hitshub.models

import com.squareup.moshi.Json


data class ChartTracksData(
    @Json(name = "data")
    override val data: MutableList<ChartTrack>
) : IRecyclerHorizontalModel

data class ChartTrack(
    @Json(name = "id")
    override val id: Long,
    @Json(name = "title")
    override val title: String,
    @Json(name = "preview")
    override val preview: String,
    @Json(name = "position")
    val position: Int,
    override val artist: Artist
) : ITrack

data class AlbumChartData(
    @Json(name = "data")
    override val data: MutableList<ChartAlbum>
) : IRecyclerHorizontalModel

data class ChartAlbum(
    @Json(name = "id")
    override val id: Long,
    @Json(name = "title")
    override val title: String,
    @Json(name = "cover")
    override val cover_url: String,
    override val artist: Artist,
    @Json(name = "tracklist")
    override val trackList_url: String
) : IAlbum

