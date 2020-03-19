package com.example.hitshub.models

import java.io.Serializable

interface ITrack : Serializable {
    val id: Long
    val title: String
    val preview: String
    val artist: Artist?
        get() = null
}
