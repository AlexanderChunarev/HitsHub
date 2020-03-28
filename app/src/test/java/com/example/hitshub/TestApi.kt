package com.example.hitshub

import com.example.hitshub.repositories.DeezerRepository
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class TestApi {

    private val deezerRepository by lazy { DeezerRepository() }

    @Test
    fun testFetchAlbumByName() {
        runBlocking {
            Assert.assertNotNull(deezerRepository.fetchAlbumByName("eminem"))
        }
    }

    @Test
    fun testFetchAlbumByChart() {
        runBlocking {
            Assert.assertNotNull(deezerRepository.fetchAlbumByChart())
        }
    }

    @Test
    fun testFetchAlbumDataById() {
        runBlocking {
            Assert.assertNotNull(deezerRepository.fetchAlbumDataById(302127))
        }
    }

    @Test
    fun testFetchTrackById() {
        runBlocking {
            Assert.assertNotNull(deezerRepository.fetchTrackById(3135556))
        }
    }

    @Test
    fun testFetchChartTracks() {
        runBlocking {
            Assert.assertNotNull(deezerRepository.fetchTracksByChart())
        }
    }

    @Test
    fun testFetchSearchedTracksByName() {
        runBlocking {
            Assert.assertNotNull(deezerRepository.fetchTrackByName("Chlorine"))
        }
    }
}
