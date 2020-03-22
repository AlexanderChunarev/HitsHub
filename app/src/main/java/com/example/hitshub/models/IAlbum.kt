package com.example.hitshub.models

interface IAlbum {
    val id: Long
    val title: String
    val cover_url: String
    val artist: Artist
    val trackList_url: String
}
