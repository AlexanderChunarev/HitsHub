package com.example.hitshub.repositories

import com.example.hitshub.builders.ServiceBuilder
import com.example.hitshub.extentions.await
import com.example.hitshub.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await

class DeezerRepository {
    private val serviceBuilder by lazy { ServiceBuilder.getInstance().buildService() }

    suspend fun fetchAlbumByName(name: String): SearchAlbumData = withContext(Dispatchers.IO) {
        serviceBuilder.searchAlbumByName(name).await()
    }

    suspend fun fetchTrackByName(name: String): TrackData = withContext(Dispatchers.IO) {
        serviceBuilder.searchTrackByName(name).await()
    }

    suspend fun fetchTracksByChart(): ChartTracksData = withContext(Dispatchers.IO) {
        serviceBuilder.getTrackByChart().await()
    }

    suspend fun fetchAlbumByChart(): ChartAlbumData = withContext(Dispatchers.IO) {
        serviceBuilder.getAlbumByChart().await()
    }

    suspend fun fetchAlbumDataById(id: Long): AlbumById = withContext(Dispatchers.IO) {
        serviceBuilder.getAlbumDataById(id).await()
    }

    suspend fun fetchTrackById(id: Long): Track = withContext(Dispatchers.IO) {
        serviceBuilder.getTrackById(id).await()
    }
}
