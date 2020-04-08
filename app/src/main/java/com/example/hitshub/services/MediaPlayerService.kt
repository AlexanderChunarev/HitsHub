package com.example.hitshub.services

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.example.hitshub.activities.MainActivity.Companion.WAKE_UP_MEDIA_PLAYER
import com.example.hitshub.extentions.next
import com.example.hitshub.fragments.BaseMediaFragment
import com.example.hitshub.media.Player
import com.example.hitshub.media.Player.Companion.ACTION_PAUSE
import com.example.hitshub.media.Player.Companion.ACTION_PLAY
import com.example.hitshub.media.Player.Companion.ACTION_PREPARE
import com.example.hitshub.media.Player.Companion.ACTION_SKIP_NEXT
import com.example.hitshub.media.Player.Companion.ACTION_SKIP_PREV
import com.example.hitshub.media.Player.Companion.TRACK_INTENT
import com.example.hitshub.models.ITrack
import com.example.hitshub.receivers.NotificationBroadcastReceiver.Companion.RECEIVE_PAUSE_ACTION_KEY
import com.example.hitshub.receivers.NotificationBroadcastReceiver.Companion.RECEIVE_PLAY_ACTION_KEY
import com.example.hitshub.receivers.NotificationBroadcastReceiver.Companion.RECEIVE_PREPARE_ACTION_KEY
import com.example.hitshub.utils.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaPlayerService : Service() {
    private val player by lazy { Player.getInstance() }
    private val notificationHelper by lazy { NotificationHelper.getInstance(this) }
    private val playerStateIntent by lazy { Intent(BaseMediaFragment::class.java.toString()) }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        player.setOnCompletionListener {
            skip(FAST_FORWARD_SELECTOR)
        }
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                startForeground(
                    NotificationHelper.NOTIFY_ID,
                    notificationHelper.createNotification(player.currentTrack)
                )
            }
        }
        when (intent.action) {
            ACTION_PREPARE -> {
                player.currentTrack = intent.getParcelableExtra(TRACK_INTENT) as ITrack
                player.playlist = intent.getParcelableArrayListExtra("playlist")!!
                prepare()
            }
            ACTION_PLAY -> {
                play()
            }
            ACTION_PAUSE -> {
                pause()
            }
            ACTION_SKIP_NEXT -> {
                skip(FAST_FORWARD_SELECTOR)
            }
            ACTION_SKIP_PREV -> {
                skip(FAST_REWIND_SELECTOR)
            }
            WAKE_UP_MEDIA_PLAYER -> {
                playerStateIntent.setTrackInfo()
                sendBroadcast(playerStateIntent)
            }
        }
        return START_STICKY
    }

    private fun prepare() {
        val playerStateIntent by lazy { Intent(BaseMediaFragment::class.java.toString()) }
        if (!player.isPlaying) {
            player.apply {
                preparePlayer()
                setOnPreparedListener {
                    start()
                    notificationHelper.updateNotification(player.currentTrack)
                    playerStateIntent.apply {
                        putExtra(RECEIVE_PREPARE_ACTION_KEY, ACTION_PREPARE)
                        setTrackInfo()
                        sendBroadcast(playerStateIntent)
                    }
                }
            }
        } else {
            player.apply {
                preparePlayer()
                setOnPreparedListener {
                    start()
                    notificationHelper.updateNotification(player.currentTrack)
                    playerStateIntent.apply {
                        putExtra(RECEIVE_PREPARE_ACTION_KEY, ACTION_PREPARE)
                        setTrackInfo()
                        sendBroadcast(playerStateIntent)
                    }
                }
            }
        }
    }

    private fun play() {
        player.start()
        playerStateIntent.apply {
            removeExtra(RECEIVE_PAUSE_ACTION_KEY)
            putExtra(RECEIVE_PLAY_ACTION_KEY, ACTION_PLAY)
        }
        sendBroadcast(playerStateIntent)
        notificationHelper.updateNotification(player.currentTrack)
    }

    private fun pause() {
        player.pause()
        playerStateIntent.apply {
            removeExtra(RECEIVE_PLAY_ACTION_KEY)
            putExtra(RECEIVE_PAUSE_ACTION_KEY, ACTION_PAUSE)
        }
        sendBroadcast(playerStateIntent)
        notificationHelper.updateNotification(player.currentTrack)
    }

    private fun skip(selector: Int) {
        player.apply {
            next(selector)
            setOnPreparedListener {
                start()
                Intent(BaseMediaFragment::class.java.toString()).apply {
                    putExtra(RECEIVE_PREPARE_ACTION_KEY, ACTION_PREPARE)
                    setTrackInfo()
                    sendBroadcast(this)
                }
                notificationHelper.updateNotification(player.currentTrack)
            }
        }
    }

    private fun next(selector: Int) {
        player.playlist.next(selector, player.currentTrack)?.let {
            player.currentTrack = it
            player.preparePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
    }

    private fun Intent.setTrackInfo() {
        this.apply {
            with(player.currentTrack) {
                putExtra(TRACK_ID, id)
                putExtra(TRACK_TITLE, title)
                putExtra(TRACK_ARTIST, artist!!.name)
                putExtra(IMAGE_URL, artist!!.pictureBig)
            }
        }
    }

    companion object {
        const val FAST_FORWARD_SELECTOR = 1
        const val FAST_REWIND_SELECTOR = -1
        const val STOP_SERVICE = "stop_foreground_service"
        const val TRACK_TITLE = "track_title"
        const val TRACK_ID = "track_id"
        const val TRACK_ARTIST = "track_artist"
        const val IMAGE_URL = "image_url"
    }
}
