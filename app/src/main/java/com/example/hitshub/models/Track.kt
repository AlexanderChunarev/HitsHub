package com.example.hitshub.models

import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Track(
    @Json(name = "id")
    override val id: Long,
    @Json(name = "title")
    override val title: String,
    @Json(name = "preview")
    override val preview: String,
    override var artist: Artist?
) : ITrack
