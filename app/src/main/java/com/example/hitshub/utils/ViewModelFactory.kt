package com.example.hitshub.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hitshub.repositories.MediaRepository
import com.example.hitshub.viewmodels.MediaViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val mediaRepository: MediaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MediaViewModel(
            mediaRepository
        ) as T
    }
}
