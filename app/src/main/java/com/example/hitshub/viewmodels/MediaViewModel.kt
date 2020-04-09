package com.example.hitshub.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hitshub.models.IAlbum
import com.example.hitshub.models.ITrack
import com.example.hitshub.models.Track
import com.example.hitshub.repositories.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaViewModel(private val mediaRepository: MediaRepository) : ViewModel() {
    val tracksLiveData by lazy { MutableLiveData<List<ITrack>>() }
    val trackLiveData by lazy { MutableLiveData<ITrack>() }
    val albumLiveData by lazy { MutableLiveData<List<IAlbum>>() }

    fun getTracks() = GlobalScope.launch {
        val response = mediaRepository.selectAll()
        withContext(Dispatchers.Main) {
            tracksLiveData.value = response
        }
    }

    fun insertTrack(track: Track) = GlobalScope.launch {
        mediaRepository.insert(track)
    }

    fun selectTrackById(id: String) = GlobalScope.launch {
        val response = mediaRepository.selectTrackById(id)
        withContext(Dispatchers.Main) {
            trackLiveData.value = response
        }
    }

    fun deleteTrack(id: String) = GlobalScope.launch {
        mediaRepository.deleteById(id)
        val response = mediaRepository.selectAll()
        withContext(Dispatchers.Main) {
            tracksLiveData.value = response
        }
    }
}
