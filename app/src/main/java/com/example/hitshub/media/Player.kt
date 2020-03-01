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
            intent.apply {
                println(this.getStringExtra(URL_KEY))
                setDataSource(this.getStringExtra(URL_KEY))
                setOnCompletionListener {
                    stop()
                    reset()
                }
                isLooping = this.getBooleanExtra(IS_LOOPING_KEY, false)
                prepare()
            }
            withContext(Dispatchers.Main) {
                audioDuration.value = TimeUnit.MILLISECONDS.toSeconds(duration.toLong())
            }
        }
    }

    companion object {
        const val PAUSE = "action.pause"
        const val PLAY = "action.play"
        const val URL_KEY = "url_key"
        const val IS_LOOPING_KEY = "is_looping_key"
        private val player by lazy { Player() }

        fun getInstance() = player
    }
}
