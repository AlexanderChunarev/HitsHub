package com.example.hitshub.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hitshub.models.AlbumData
import com.example.hitshub.models.Track

class DeezerViewModel: ViewModel() {
    val trackLiveData by lazy { MutableLiveData<List<Track>>() }
    val albumkLiveData by lazy { MutableLiveData<AlbumData>() }
}