package com.example.hitshub.models

import android.os.Parcelable

interface ITrack : Parcelable {
    val id: Long
    val title: String
    val preview: String
    val artist: Artist?
        get() = null
}
