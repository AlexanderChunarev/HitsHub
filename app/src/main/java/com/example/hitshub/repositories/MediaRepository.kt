package com.example.hitshub.repositories

import com.example.hitshub.databases.RepoDataBase
import com.example.hitshub.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaRepository(private val repoDataBase: RepoDataBase) {

    suspend fun insert(track: Track) = withContext(Dispatchers.IO) {
        repoDataBase.trackDao().insert(track = track)
    }

    suspend fun selectAll() = withContext(Dispatchers.IO) {
        repoDataBase.trackDao().selectAllTracks()
    }

    suspend fun selectTrackById(id: String) = withContext(Dispatchers.IO) {
        repoDataBase.trackDao().selectTrack(id = id)
    }

    suspend fun deleteById(id: String) = withContext(Dispatchers.IO) {
        repoDataBase.trackDao().delete(id = id)
    }

    companion object {
        @Volatile
        private var instance: MediaRepository? = null

        fun getInstance(repoDb: RepoDataBase) =
            instance ?: synchronized(this) {
                instance
                    ?: MediaRepository(repoDb).also { instance = it }
            }
    }
}
