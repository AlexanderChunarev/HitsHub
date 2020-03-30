package com.example.hitshub.media

import android.media.MediaPlayer
import java.io.IOException

class Player : MediaPlayer() {

    fun preparePlayer(url: String) {
        try {
            player!!.apply {
                reset()
                setDataSource(url)
                prepareAsync()
            }
        } catch (e: IllegalArgumentException) {
        } catch (e: IllegalStateException) {
        } catch (e: IOException) {
        }
    }

    companion object {
        const val ACTION_PREPARE = "action.prepare"
        const val ACTION_PAUSE = "action.pause"
        const val ACTION_PLAY = "action.play"
        const val ACTION_SKIP_NEXT = "action.fastForward"
        const val ACTION_SKIP_PREV = "action.fastRewind"
        const val TRACK_INTENT = "track_key"
        private var player: Player? = null

        fun getInstance(): Player {
            if (player == null) {
                player = Player()
            }
            return player!!
        }
    }
}
