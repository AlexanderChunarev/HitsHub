package com.example.hitshub.media

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit

class Player : MediaPlayer() {
    private val audioDuration = MutableLiveData<Long>()
    val _prepareState = MutableLiveData<Boolean>()
    val prepareState: LiveData<Boolean> get() = _prepareState

    fun getTrackDuration() = audioDuration

    suspend fun prepareMediaPlayer(url: String) = withContext(Dispatchers.IO) {
        player.apply {
            try {
                reset()
                setDataSource(url)
                prepare()
            } catch (e: IllegalArgumentException) {
            } catch (e: IllegalStateException) {
            } catch (e: IOException) {
            }
            setOnCompletionListener {
                stop()
                audioDuration.value = RESET
            }
            setOnPreparedListener {
                start()
            }
            withContext(Dispatchers.Main) {
                audioDuration.value = TimeUnit.MILLISECONDS.toSeconds(duration.toLong())
            }
        }
    }

    companion object {
        const val ACTION_PAUSE = "action.pause"
        const val ACTION_PLAY = "action.play"
        const val ACTION_FAST_FORWARD = "action.fastForward"
        const val ACTION_FAST_REWIND = "action.fastRewind"
        const val TRACK_INTENT = "track_key"
        const val RESET = 0.toLong()
        private var player: Player? = null

        fun getInstance(): Player {
            if (player == null) {
                player = Player()
            }
            return player!!
        }
    }
}
