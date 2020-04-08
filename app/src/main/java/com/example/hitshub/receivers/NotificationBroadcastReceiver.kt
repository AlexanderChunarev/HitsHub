package com.example.hitshub.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startForegroundService
import com.example.hitshub.fragments.BaseMediaFragment
import com.example.hitshub.media.Player.Companion.ACTION_PAUSE
import com.example.hitshub.media.Player.Companion.ACTION_PLAY
import com.example.hitshub.media.Player.Companion.ACTION_SKIP_NEXT
import com.example.hitshub.media.Player.Companion.ACTION_SKIP_PREV
import com.example.hitshub.services.MediaPlayerService
import com.example.hitshub.services.MediaPlayerService.Companion.STOP_SERVICE

class NotificationBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val serviceIntent by lazy { Intent(context, MediaPlayerService::class.java) }
        val playerStateIntent by lazy { Intent(BaseMediaFragment::class.java.toString()) }

        when (intent!!.action) {
            ACTION_PAUSE -> {
                playerStateIntent.putExtra(RECEIVE_PAUSE_ACTION_KEY, ACTION_PAUSE)
                context.sendBroadcast(playerStateIntent)
                serviceIntent.action = ACTION_PAUSE
                startForegroundService(context, serviceIntent)
            }
            ACTION_PLAY -> {
                playerStateIntent.putExtra(RECEIVE_PLAY_ACTION_KEY, ACTION_PLAY)
                context.sendBroadcast(playerStateIntent)
                serviceIntent.action = ACTION_PLAY
                startForegroundService(context, serviceIntent)
            }
            ACTION_SKIP_NEXT -> {
                serviceIntent.action = ACTION_SKIP_NEXT
                startForegroundService(context, serviceIntent)
            }
            ACTION_SKIP_PREV -> {
                serviceIntent.action = ACTION_SKIP_PREV
                startForegroundService(context, serviceIntent)
            }
            STOP_SERVICE -> {
                context.stopService(Intent(context, MediaPlayerService::class.java))
            }
        }
    }

    companion object {
        const val RECEIVE_PREPARE_ACTION_KEY = "pause_receiver_key"
        const val RECEIVE_PAUSE_ACTION_KEY = "pause_receiver_key"
        const val RECEIVE_PLAY_ACTION_KEY = "play_receiver_key"
    }
}
