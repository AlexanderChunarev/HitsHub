package com.example.hitshub.models

import com.squareup.moshi.Json

data class TrackData(
    @Json(name = "data")
    override val data: MutableList<Track>
) : IRecyclerHorizontalModel

data class SearchAlbumData(
    @Json(name = "data")
    override val data: MutableList<Album>
) : IRecyclerHorizontalModel
