package com.example.hitshub.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hitshub.models.*
import com.example.hitshub.repositories.DeezerRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DeezerViewModel : ViewModel() {
    private val topTrackLiveData by lazy { MutableLiveData<ChartTracksData>() }
    private val topAlbumkLiveData by lazy { MutableLiveData<AlbumChartData>() }
    private val getTrackByName by lazy { MutableLiveData<TrackData>() }
    private val getAlbymById by lazy { MutableLiveData<Album>() }
    private val getAlbumByName by lazy { MutableLiveData<AlbumData>() }
    private val getTrackById by lazy { MutableLiveData<Track>() }
    private val deezerRepo by lazy { DeezerRepository() }

    fun getTopTracks() = GlobalScope.launch {
        topTrackLiveData.value = deezerRepo.fetchTracksByChart()
    }

    fun getTopAlbums() = GlobalScope.launch {
        topAlbumkLiveData.value = deezerRepo.fetchAlbumByChart()
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
