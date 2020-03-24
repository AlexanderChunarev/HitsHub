package com.example.hitshub.media

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class Player : MediaPlayer() {
    private val audioDuration = MutableLiveData<Long>()
    val _showSpinner = MutableLiveData<Boolean>()
    val showSpinner: LiveData<Boolean> get() = _showSpinner

    fun getTrackDuration() = audioDuration

    suspend fun prepareMediaPlayer(url: String) = withContext(Dispatchers.IO) {
        player.apply {
            reset()
            setDataSource(url)
            setOnCompletionListener {
                stop()
                reset()
                audioDuration.value = RESET
            }
            prepare()
            withContext(Dispatchers.Main) {
                audioDuration.value = TimeUnit.MILLISECONDS.toSeconds(duration.toLong())
            }
        }
    }

    companion object {
        const val PAUSE = "action.pause"
        const val PLAY = "action.play"
        const val FAST_FORWARD = "action.fastForward"
        const val FAST_REWIND = "action.fastRewind"
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
