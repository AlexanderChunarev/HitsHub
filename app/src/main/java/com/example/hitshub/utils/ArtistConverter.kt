package com.example.hitshub.utils

import androidx.room.TypeConverter
import com.example.hitshub.models.Artist
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

class ArtistConverter {
    companion object {
        val moshi = Moshi.Builder().build()
        var jsonAdapter: JsonAdapter<Artist> = moshi.adapter(Artist::class.java)
        @TypeConverter
        @JvmStatic
        fun toOwner(artist: Artist) = jsonAdapter.toJson(artist)

        @TypeConverter
        @JvmStatic
        fun fromOwner(artist: String) = jsonAdapter.fromJson(artist)
    }
}
