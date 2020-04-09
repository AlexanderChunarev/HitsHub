package com.example.hitshub.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "liked_track")
data class Track(
    @Json(name = "id")
    @ColumnInfo
    @PrimaryKey
    override var id: Long,
    @Json(name = "title")
    @ColumnInfo
    override var title: String,
    @Json(name = "preview")
    @ColumnInfo
    override var preview: String,
    @ColumnInfo
    override var artist: Artist?
) : ITrack {
    constructor() : this(0, "", "", null)
}
