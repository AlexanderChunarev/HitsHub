package com.example.hitshub.utils

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.example.hitshub.R
import com.example.hitshub.application.App
import com.example.hitshub.media.Player
import com.example.hitshub.receivers.NotificationBroadcastReceiver
import com.example.hitshub.receivers.NotificationBroadcastReceiver.Companion.RECEIVE_PAUSE_ACTION_KEY
import com.example.hitshub.receivers.NotificationBroadcastReceiver.Companion.RECEIVE_PLAY_ACTION_KEY

class NotificationHelper(private val context: Context) {
    private val player by lazy { Player.getInstance() }
    private val mediaSession = MediaSessionCompat(context, "tag")

    fun createNotification(): Notification {
        val pauseOrStartIntent = Intent(context, NotificationBroadcastReceiver::class.java)
        val pauseOrStartPendingIntent: PendingIntent?
        val isPlayOrPause: Int

        if (player.isPlaying) {
            isPlayOrPause = R.drawable.ic_pause
            pauseOrStartIntent.putExtra(RECEIVE_PAUSE_ACTION_KEY, Player.PAUSE)
        } else {
            isPlayOrPause = R.drawable.ic_play
            pauseOrStartIntent.putExtra(RECEIVE_PLAY_ACTION_KEY, Player.PLAY)
        }
        pauseOrStartPendingIntent = createAction(pauseOrStartIntent)

        return NotificationCompat.Builder(context, App.CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_play)
            .setContentTitle("track_title")
            .setContentText("album_name")
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.common_full_open_on_phone))
            .addAction(R.drawable.ic_fast_rewind, "Rewind", null)
            .addAction(isPlayOrPause, "Previous", pauseOrStartPendingIntent)
            .addAction(R.drawable.ic_fast_forward, "Forward", null)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(
                    1, 2
                ).setMediaSession(mediaSession.sessionToken)
            )
            .setSubText("Sub Text")
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createAction(intent: Intent): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            PENDING_INTENT_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun updateNotification() {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFY_ID, createNotification())
    }

    companion object {
        const val NOTIFY_ID = 1
        const val PENDING_INTENT_REQUEST_CODE = 0

        private var notificationHelped: NotificationHelper? = null

        fun getInstance(context: Context): NotificationHelper {
            if (notificationHelped == null) {
                notificationHelped = NotificationHelper(context)
            }
            return notificationHelped!!
        }
    }
}
