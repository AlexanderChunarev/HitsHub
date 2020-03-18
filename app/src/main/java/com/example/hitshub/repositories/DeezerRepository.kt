package com.example.hitshub.repositories

import com.example.hitshub.builders.ServiceBuilder
import com.example.hitshub.models.*
import com.example.hitshub.models.album.Album
import com.example.hitshub.models.chart.album.AlbumChartData
import com.example.hitshub.models.chart.track.ChartTracksData
import com.example.hitshub.models.search.album.AlbumData
import com.example.hitshub.models.search.track.TrackData
import com.example.hitshub.models.track.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await

class DeezerRepository {
    private val serviceBuilder by lazy { ServiceBuilder.getInstance().buildService() }

    suspend fun fetchAlbumByName(name: String): AlbumData = withContext(Dispatchers.IO) {
        serviceBuilder.searchAlbumByName(name).await()
    }

    suspend fun fetchTrackByName(name: String): TrackData = withContext(Dispatchers.IO) {
        serviceBuilder.searchTrackByName(name).await()
    }

    suspend fun fetchTracksByChart(): ChartTracksData = withContext(Dispatchers.IO) {
        serviceBuilder.getTrackByChart().await()
    }

    suspend fun fetchAlbumByChart(): AlbumChartData = withContext(Dispatchers.IO) {
        serviceBuilder.getAlbumByChart().await()
    }

    suspend fun fetchAlbumDataById(id: Long): Album = withContext(Dispatchers.IO) {
        serviceBuilder.getAlbumDataById(id).await()
    }

    suspend fun fetchTrackById(id: Long): Track = withContext(Dispatchers.IO) {
        serviceBuilder.getTrackById(id).await()
    }
}
