package com.example.hitshub.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.hitshub.fragments.PlayerFragment
import com.example.hitshub.media.Player
import com.example.hitshub.media.Player.Companion.ACTION_FAST_FORWARD
import com.example.hitshub.media.Player.Companion.ACTION_FAST_REWIND
import com.example.hitshub.media.Player.Companion.ACTION_PAUSE
import com.example.hitshub.media.Player.Companion.ACTION_PLAY
import com.example.hitshub.media.Player.Companion.TRACK_INTENT
import com.example.hitshub.models.ITrack
import com.example.hitshub.services.MediaPlayerService
import com.example.hitshub.services.MediaPlayerService.Companion.STOP_SERVICE
import com.example.hitshub.utils.NotificationHelper

class NotificationBroadcastReceiver : BroadcastReceiver() {
    private val player by lazy { Player.getInstance() }

    override fun onReceive(context: Context, intent: Intent?) {
        val notificationHelper by lazy { NotificationHelper.getInstance(context) }
        val playerStateIntent by lazy { Intent(PlayerFragment::class.java.toString()) }

        when (intent!!.action) {
            ACTION_PAUSE -> {
                player.pause()
                notificationHelper.updateNotification(
                    intent.getSerializableExtra(TRACK_INTENT) as ITrack, player.isPlaying
                )
                playerStateIntent.putExtra(RECEIVE_PAUSE_ACTION_KEY, ACTION_PAUSE)
            }
            ACTION_PLAY -> {
                player.start()
                notificationHelper.updateNotification(
                    intent.getSerializableExtra(TRACK_INTENT) as ITrack, player.isPlaying
                )
                playerStateIntent.putExtra(RECEIVE_PLAY_ACTION_KEY, ACTION_PLAY)
            }
            ACTION_FAST_FORWARD -> {
                playerStateIntent.putExtra(RECEIVE_FAST_FORWARD_ACTION_KEY, ACTION_FAST_FORWARD)
            }
            ACTION_FAST_REWIND -> {
                playerStateIntent.putExtra(RECEIVE_FAST_REWIND_ACTION_KEY, ACTION_FAST_REWIND)
            }
            STOP_SERVICE -> {
                context.stopService(Intent(context, MediaPlayerService::class.java))
            }
        }
        context.sendBroadcast(playerStateIntent)
    }

    companion object {
        const val RECEIVE_PAUSE_ACTION_KEY = "pause_receiver_key"
        const val RECEIVE_PLAY_ACTION_KEY = "play_receiver_key"
        const val RECEIVE_FAST_FORWARD_ACTION_KEY = "fast_forward_receiver_key"
        const val RECEIVE_FAST_REWIND_ACTION_KEY = "fast_rewind_receiver_key"
    }
}
