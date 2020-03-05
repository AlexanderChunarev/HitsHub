package com.example.hitshub

import com.example.hitshub.repositories.DeezerRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class TestApi {

    val deezerRepository by lazy { DeezerRepository() }

    @Test
    fun testFetchAlbumByName(){
        runBlocking {
            deezerRepository.fetchAlbumByName("eminem").searchedAlbums.forEach {
                println(it)
            }
            Assert.assertNotNull(deezerRepository.fetchAlbumByName("eminem"))
        }
    }

    @Test
    fun testFetchAlbumByChart(){
        runBlocking {
            deezerRepository.fetchAlbumByChart().data.forEach {
                println(it)
            }
        }
    }

    @Test
    fun testFetchAlbumDataById(){
        runBlocking {
            deezerRepository.fetchAlbumDataById(302127).tracks.data.forEach {
                println(it)
            }
        }
    }

    @Test
    fun testFetchChartTracks(){
        runBlocking {
            deezerRepository.fetchTracksByChart().data.forEach {
                println(it)
            }
        }
    }

    @Test
    fun testFetchSearchedTracksByName(){
        runBlocking {
            deezerRepository.fetchTrackByName("Chlorine").data.forEach {
                println(it)
            }
        }
    }


}