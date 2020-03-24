package com.example.hitshub.utils

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.example.hitshub.R
import com.example.hitshub.application.App
import com.example.hitshub.media.Player
import com.example.hitshub.media.Player.Companion.FAST_FORWARD
import com.example.hitshub.media.Player.Companion.FAST_REWIND
import com.example.hitshub.media.Player.Companion.PAUSE
import com.example.hitshub.media.Player.Companion.PLAY
import com.example.hitshub.media.Player.Companion.TRACK_INTENT
import com.example.hitshub.models.ITrack
import com.example.hitshub.receivers.NotificationBroadcastReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class NotificationHelper(private val context: Context) {
    private val mediaSession = MediaSessionCompat(context, "tag")

    suspend fun createNotification(track: ITrack, player: Player): Notification =
        withContext(Dispatchers.Main) {
            val pauseOrStartIntent = Intent(context, NotificationBroadcastReceiver::class.java)
            val fastRewindIntent = Intent(context, NotificationBroadcastReceiver::class.java)
            val fastForwardIntent = Intent(context, NotificationBroadcastReceiver::class.java)
            val isPlayOrPause: Int

            if (player.isPlaying) {
                isPlayOrPause = R.drawable.ic_pause
                pauseOrStartIntent.action = PAUSE
            } else {
                isPlayOrPause = R.drawable.ic_play
                pauseOrStartIntent.action = PLAY
            }
            pauseOrStartIntent.putExtra(TRACK_INTENT, track)
            fastForwardIntent.action = FAST_FORWARD
            fastRewindIntent.action = FAST_REWIND

            NotificationCompat.Builder(context, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_play)
                .setContentTitle(track.title)
                .setContentText(track.artist!!.name)
                .setLargeIcon(getIconBitmap(track))
                .addAction(
                    R.drawable.ic_fast_rewind,
                    "Rewind",
                    createAction(fastRewindIntent, REWIND_REQUEST_CODE)
                )
                .addAction(
                    isPlayOrPause,
                    "Previous",
                    createAction(pauseOrStartIntent, PLAY_PAUSE_REQUEST_CODE)
                )
                .addAction(
                    R.drawable.ic_fast_forward,
                    "Forward",
                    createAction(fastForwardIntent, FORWARD_REQUEST_CODE)
                )
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

    private suspend fun getIconBitmap(track: ITrack): Bitmap = withContext(Dispatchers.IO) {
        BitmapFactory.decodeStream(URL(track.artist!!.picture).openConnection().getInputStream())
    }

    private fun createAction(intent: Intent, requestCode: Int): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun updateNotification(track: ITrack, player: Player) = GlobalScope.launch {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFY_ID, createNotification(track, player))
    }

    companion object {
        const val NOTIFY_ID = 1
        const val PLAY_PAUSE_REQUEST_CODE = 100
        const val FORWARD_REQUEST_CODE = 101
        const val REWIND_REQUEST_CODE = 102

        private var notificationHelped: NotificationHelper? = null

        fun getInstance(context: Context): NotificationHelper {
            if (notificationHelped == null) {
                notificationHelped = NotificationHelper(context)
            }
            return notificationHelped!!
        }
    }
}
