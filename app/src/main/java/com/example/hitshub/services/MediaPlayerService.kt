package com.example.hitshub.services

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.example.hitshub.media.Player
import com.example.hitshub.utils.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaPlayerService : Service() {
    private val player by lazy { Player.getInstance() }
    private val notificationHelped by lazy { NotificationHelper.getInstance(this) }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        GlobalScope.launch {
            player.prepareMediaPlayer(intent)
            player.start()
            withContext(Dispatchers.Main) {
                startForeground(
                    NotificationHelper.NOTIFY_ID,
                    notificationHelped.createNotification()
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
