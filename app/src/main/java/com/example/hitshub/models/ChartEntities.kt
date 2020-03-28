package com.example.hitshub.models

import com.squareup.moshi.Json

data class ChartTracksData(
    @Json(name = "data")
    override val data: MutableList<Track>
) : IRecyclerHorizontalModel

data class ChartAlbumData(
    @Json(name = "data")
    override val data: MutableList<Album>
) : IRecyclerHorizontalModel
