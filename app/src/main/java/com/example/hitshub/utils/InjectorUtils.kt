package com.example.hitshub.utils

import android.content.Context
import com.example.hitshub.databases.RepoDataBase
import com.example.hitshub.repositories.MediaRepository

object InjectorUtils {

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val quoteRepository = MediaRepository.getInstance(RepoDataBase.getInstance(context))
        return ViewModelFactory(quoteRepository)
    }
}
