package com.example.hitshub.models

import java.io.Serializable

interface IAlbum : Serializable {
    val id: Long
    val title: String
    val cover_url: String
    val artist: Artist
    val albumTracks: AlbumTracks?
        get() = null
    val trackList_url: String?
        get() = null
}
