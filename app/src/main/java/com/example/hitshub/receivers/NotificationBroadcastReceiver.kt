package com.example.hitshub.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.hitshub.media.Player
import com.example.hitshub.utils.NotificationHelper

class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val notificationHelped by lazy { NotificationHelper.getInstance(context) }
        if (intent?.getStringExtra(RECEIVE_PAUSE_ACTION_KEY) == Player.PAUSE) {
            Player.getInstance().pause()
            notificationHelped.updateNotification()
        } else if (intent?.getStringExtra(RECEIVE_PLAY_ACTION_KEY) == Player.PLAY) {
            Player.getInstance().start()
            notificationHelped.updateNotification()
        }
    }

    companion object {
        const val RECEIVE_PAUSE_ACTION_KEY = "pause_receiver_key"
        const val RECEIVE_PLAY_ACTION_KEY = "play_receiver_key"
    }
}
