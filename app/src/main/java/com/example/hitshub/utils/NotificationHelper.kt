package com.example.hitshub.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.example.hitshub.R
import com.example.hitshub.activities.MainActivity
import com.example.hitshub.activities.MainActivity.Companion.WAKE_UP_MEDIA_PLAYER
import com.example.hitshub.application.App
import com.example.hitshub.media.Player
import com.example.hitshub.media.Player.Companion.ACTION_PAUSE
import com.example.hitshub.media.Player.Companion.ACTION_PLAY
import com.example.hitshub.media.Player.Companion.ACTION_SKIP_NEXT
import com.example.hitshub.media.Player.Companion.ACTION_SKIP_PREV
import com.example.hitshub.models.ITrack
import com.example.hitshub.receivers.NotificationBroadcastReceiver
import com.example.hitshub.services.MediaPlayerService.Companion.STOP_SERVICE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class NotificationHelper(private val context: Context) {
    private val mediaSession = MediaSessionCompat(context, "tag")
    private val player by lazy { Player.getInstance() }

    suspend fun createNotification(track: ITrack) =
        withContext(Dispatchers.IO) {
            val pauseOrStartIntent = Intent(context, NotificationBroadcastReceiver::class.java)
            val fastRewindIntent = Intent(context, NotificationBroadcastReceiver::class.java)
            val fastForwardIntent = Intent(context, NotificationBroadcastReceiver::class.java)
            val closeIntent = Intent(context, NotificationBroadcastReceiver::class.java)
            val resultIntent = Intent(context, MainActivity::class.java)
            val drawable: Int

            if (player.isPlaying) {
                drawable = R.drawable.ic_pause
                pauseOrStartIntent.action = ACTION_PAUSE
            } else {
                drawable = R.drawable.ic_play
                pauseOrStartIntent.action = ACTION_PLAY
            }
            fastForwardIntent.action = ACTION_SKIP_NEXT
            fastRewindIntent.action = ACTION_SKIP_PREV
            closeIntent.action = STOP_SERVICE
            resultIntent.action = WAKE_UP_MEDIA_PLAYER

            NotificationCompat.Builder(context, App.CHANNEL_1_ID)
                .setAutoCancel(true)
                .setOngoing(true)
                .setSmallIcon(R.drawable.headphones_logo)
                .setContentTitle(track.title)
                .setContentText(track.artist!!.name)
                .setLargeIcon(getIconBitmap(track))
                .addAction(
                    R.drawable.ic_fast_rewind,
                    null,
                    createPendingIntent(fastRewindIntent, REWIND_REQUEST_CODE)
                )
                .addAction(
                    drawable,
                    null,
                    createPendingIntent(pauseOrStartIntent, PLAY_PAUSE_REQUEST_CODE)
                )
                .addAction(
                    R.drawable.ic_fast_forward,
                    null,
                    createPendingIntent(fastForwardIntent, FORWARD_REQUEST_CODE)
                )
                .addAction(
                    R.drawable.ic_close,
                    null,
                    createPendingIntent(closeIntent, CLOSE_REQUEST_CODE)
                )
                .setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(
                        1, 2, 3
                    ).setMediaSession(mediaSession.sessionToken)
                )
                .setOngoing(true)
                .setContentIntent(
                    PendingIntent.getActivity(
                        context,
                        ACTIVITY_REQUEST_CODE,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
        }!!

    private fun getIconBitmap(track: ITrack): Bitmap {
        return BitmapFactory.decodeStream(URL(track.artist!!.picture).openConnection().getInputStream())
    }

    private fun createPendingIntent(intent: Intent, requestCode: Int): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun updateNotification(track: ITrack) = GlobalScope.launch {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFY_ID, createNotification(track))
    }

    companion object {
        const val NOTIFY_ID = 1
        const val PLAY_PAUSE_REQUEST_CODE = 100
        const val FORWARD_REQUEST_CODE = 101
        const val REWIND_REQUEST_CODE = 102
        const val CLOSE_REQUEST_CODE = 400
        const val ACTIVITY_REQUEST_CODE = 1

        private var notificationHelped: NotificationHelper? = null

        fun getInstance(context: Context): NotificationHelper {
            if (notificationHelped == null) {
                notificationHelped = NotificationHelper(context)
            }
            return notificationHelped!!
        }
    }
}
