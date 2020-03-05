package com.example.hitshub.repositories

import com.example.hitshub.builders.ServiceBuilder
import com.example.hitshub.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await

class DeezerRepository {
    private val serviceBuilder by lazy { ServiceBuilder.getInstance().buildService() }

    suspend fun fetchAlbumByName(name: String): SearchAlbums = withContext(Dispatchers.IO) {
        serviceBuilder.searchAlbumByName(name).await()
    }

    suspend fun fetchTrackByName(name: String): SearchedTrackData = withContext(Dispatchers.IO) {
        serviceBuilder.searchTrackByName(name).await()
    }

    suspend fun fetchTracksByChart(): TracksData = withContext(Dispatchers.IO) {
        serviceBuilder.getTrackByChart().await()
    }

    suspend fun fetchAlbumByChart(): ChartAlbums = withContext(Dispatchers.IO) {
        serviceBuilder.getAlbumByChart().await()
    }

    suspend fun fetchAlbumDataById(id : Long): AlbumData = withContext(Dispatchers.IO) {
        serviceBuilder.getAlbumDataById(id).await()
    }

}
