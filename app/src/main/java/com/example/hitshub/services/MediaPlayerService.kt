package com.example.hitshub.services

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.example.hitshub.media.Player
import com.example.hitshub.media.Player.Companion.TRACK_INTENT
import com.example.hitshub.models.ITrack
import com.example.hitshub.utils.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaPlayerService : Service() {
    private val player by lazy { Player.getInstance() }
    private val notificationHelper by lazy { NotificationHelper.getInstance(this) }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        GlobalScope.launch {
            val track = intent.getSerializableExtra(TRACK_INTENT) as ITrack
            withContext(Dispatchers.Main) {
                player._showSpinner.value = true
            }
            player.prepareMediaPlayer(track.preview)
            withContext(Dispatchers.Main) {
                player._showSpinner.value = false
            }
            player.start()
            withContext(Dispatchers.Main) {
                startForeground(
                    NotificationHelper.NOTIFY_ID,
                    notificationHelper.createNotification(track, player)
                )
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
    }
}
