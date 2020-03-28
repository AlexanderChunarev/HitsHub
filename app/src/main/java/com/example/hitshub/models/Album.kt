package com.example.hitshub.models

import com.squareup.moshi.Json

data class Album(
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
