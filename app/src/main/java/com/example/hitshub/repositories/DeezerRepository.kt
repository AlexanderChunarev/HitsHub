package com.example.hitshub.repositories


import com.example.hitshub.builders.ServiceBuilder
import com.example.hitshub.models.Album
import com.example.hitshub.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeezerRepository {
    private val serviceBuilder by lazy { ServiceBuilder.getInstance() }

    suspend fun fetchAlbumById(id: String): Album = withContext(Dispatchers.IO) {
        var album: Album
        serviceBuilder.buildService().apply {
            album = getAlbumById(id).execute().body()!!
        }
        album
    }
    suspend fun fetchTrackById(id: String): Track = withContext(Dispatchers.IO) {
        var track: Track
        serviceBuilder.buildService().apply {
            track = getTrackById(id).execute().body()!!
        }
        track
    }
}