package com.example.hitshub.media

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hitshub.extentions.next
import com.example.hitshub.models.ITrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.concurrent.TimeUnit

class Player : MediaPlayer() {
    lateinit var track: ITrack
    lateinit var playlist: MutableList<ITrack>
    val _prepareState = MutableLiveData<Boolean>()
    val prepareState: LiveData<Boolean> get() = _prepareState
    val _duration = MutableLiveData<Int>()
    val trackDuration: LiveData<Int> get() = _duration

    suspend fun prepareMediaPlayer() = withContext(Dispatchers.IO) {
        player.run {
            try {
                reset()
                setDataSource(track.preview)
                prepare()
                start()
            } catch (e: IllegalArgumentException) {
            } catch (e: IllegalStateException) {
            } catch (e: IOException) {
            }
            withContext(Dispatchers.Main) {
                _duration.value =
                    TimeUnit.MILLISECONDS.toSeconds(this@run!!.duration.toLong()).toInt()
            }
        }
    }

    fun next(selector: Int) {
        player!!.playlist.next(selector, track).run {
            if (this != null) {
                track = this
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
