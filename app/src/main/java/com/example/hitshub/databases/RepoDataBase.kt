package com.example.hitshub.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hitshub.dao.TrackDao
import com.example.hitshub.models.Track
import com.example.hitshub.utils.ArtistConverter

@Database(
    entities = [Track::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(ArtistConverter::class)
abstract class RepoDataBase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    // abstract fun userDao(): UserDao

    companion object {
        private const val DATABASE = "media_db"

        @Volatile
        private var instance: RepoDataBase? = null

        fun getInstance(context: Context): RepoDataBase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): RepoDataBase {
            return Room.databaseBuilder(context, RepoDataBase::class.java, DATABASE)
                .build()
        }
    }
}
