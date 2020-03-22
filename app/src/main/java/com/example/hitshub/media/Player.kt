package com.example.hitshub.media

import android.content.Intent
import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class Player : MediaPlayer() {
    private val audioDuration = MutableLiveData<Long>()

    fun getTrackDuration() = audioDuration

    suspend fun prepareMediaPlayer(intent: Intent) = withContext(Dispatchers.IO) {
        println(true)
        player.apply {
            reset()
            setDataSource(intent.getStringExtra(TRACK_URL))
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
        const val TRACK_URL = "url_key"
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
