package com.example.hitshub.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hitshub.models.*
import com.example.hitshub.repositories.DeezerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeezerViewModel : ViewModel() {
    val topTrackLiveData by lazy { MutableLiveData<ChartTracksData>() }
    val topAlbumLiveData by lazy { MutableLiveData<AlbumChartData>() }
    val getTrackByName by lazy { MutableLiveData<TrackData>() }
    val getAlbymById by lazy { MutableLiveData<Album>() }
    val getAlbumByName by lazy { MutableLiveData<AlbumData>() }
    val getTrackById by lazy { MutableLiveData<Track>() }
    val deezerRepo by lazy { DeezerRepository() }

    fun getTopTracks() = GlobalScope.launch {
        val response = deezerRepo.fetchTracksByChart()
        withContext(Dispatchers.Main) {
            topTrackLiveData.value = response
        }
    }

    fun getTopAlbums() = GlobalScope.launch {
        val response = deezerRepo.fetchAlbumByChart()
        withContext(Dispatchers.Main) {
            topAlbumLiveData.value = response
        }
    }

    fun getTrackByName(name: String) = GlobalScope.launch {
        getTrackByName.value = deezerRepo.fetchTrackByName(name)
    }

    fun getAlbumById(id: Long) = GlobalScope.launch {
        getAlbymById.value = deezerRepo.fetchAlbumDataById(id)
    }

    fun getAlbumByName(name: String) = GlobalScope.launch {
        getAlbumByName.value = deezerRepo.fetchAlbumByName(name)
    }

    fun getTrackById(id: Long) = GlobalScope.launch {
        getTrackById.value = deezerRepo.fetchTrackById(id)
    }
}
