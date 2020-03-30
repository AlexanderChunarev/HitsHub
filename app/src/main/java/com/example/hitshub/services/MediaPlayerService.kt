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
import com.example.hitshub.receivers.NotificationBroadcastReceiver.Companion.RECEIVE_SKIP_NEXT_ACTION_KEY
import com.example.hitshub.receivers.NotificationBroadcastReceiver.Companion.RECEIVE_SKIP_PREV_ACTION_KEY
import com.example.hitshub.utils.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MediaPlayerService : Service() {
    private val player by lazy { Player.getInstance() }
    private val notificationHelper by lazy { NotificationHelper.getInstance(this) }
    private val playerStateIntent by lazy { Intent(BaseMediaFragment::class.java.toString()) }
    private lateinit var playlist: ArrayList<ITrack>
    private lateinit var serviceTrack: ITrack

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            ACTION_PREPARE -> {
                serviceTrack = intent.getParcelableExtra(TRACK_INTENT) as ITrack
                playlist = intent.getParcelableArrayListExtra("playlist")!!
                if (!player.isPlaying) {
                    player.preparePlayer(serviceTrack.preview)
                    player.setOnPreparedListener {
                        player.start()
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                startForeground(
                                    NotificationHelper.NOTIFY_ID,
                                    notificationHelper.createNotification(serviceTrack)
                                )
                            }
                        }
                    }
                    playerStateIntent.setTrackInfo()
                    sendBroadcast(playerStateIntent)
                } else {
                    player.preparePlayer(serviceTrack.preview)
                    player.setOnPreparedListener {
                        player.start()
                        notificationHelper.updateNotification(serviceTrack)
                    }
                    playerStateIntent.setTrackInfo()
                    sendBroadcast(playerStateIntent)
                }
            }
            ACTION_PLAY -> {
                play()
                playerStateIntent.apply {
                    removeExtra(RECEIVE_PAUSE_ACTION_KEY)
                    putExtra(RECEIVE_PLAY_ACTION_KEY, ACTION_PLAY)
                    setTrackInfo()
                }
                sendBroadcast(playerStateIntent)
            }
            ACTION_PAUSE -> {
                pause()
                playerStateIntent.apply {
                    removeExtra(RECEIVE_PLAY_ACTION_KEY)
                    putExtra(RECEIVE_PAUSE_ACTION_KEY, ACTION_PAUSE)
                    setTrackInfo()
                }
                sendBroadcast(playerStateIntent)
            }
            ACTION_SKIP_NEXT -> {
                skipNext(FAST_FORWARD_SELECTOR)
                playerStateIntent.apply {
                    putExtra(RECEIVE_SKIP_NEXT_ACTION_KEY, ACTION_SKIP_NEXT)
                    setTrackInfo()
                }
                sendBroadcast(playerStateIntent)
            }
            ACTION_SKIP_PREV -> {
                skipNext(FAST_REWIND_SELECTOR)
                playerStateIntent.apply {
                    putExtra(RECEIVE_SKIP_PREV_ACTION_KEY, ACTION_SKIP_PREV)
                    setTrackInfo()
                }
                sendBroadcast(playerStateIntent)
            }
            WAKE_UP_MEDIA_PLAYER -> {
                playerStateIntent.setTrackInfo()
                sendBroadcast(playerStateIntent)
            }
        }
        return START_STICKY
    }

    private fun play() {
        player.start()
        notificationHelper.updateNotification(serviceTrack)
    }

    private fun pause() {
        player.pause()
        notificationHelper.updateNotification(serviceTrack)
    }

    private fun skipNext(selector: Int) {
        player.apply {
            next(selector)
            setOnPreparedListener {
                start()
                notificationHelper.updateNotification(serviceTrack)
            }
        }
    }

    private fun next(selector: Int) {
        playlist.next(selector, serviceTrack).run {
            serviceTrack = this
            player.preparePlayer(serviceTrack.preview)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
        stopSelf()
    }

    private fun Intent.setTrackInfo() {
        this.apply {
            putExtra(TRACK_ID, serviceTrack.id.toString())
            putExtra(TRACK_TITLE, serviceTrack.title)
            putExtra(TRACK_ARTIST, serviceTrack.artist!!.name)
            putExtra(IMAGE_URL, serviceTrack.artist!!.pictureBig)
        }
    }

    companion object {
        const val FAST_FORWARD_SELECTOR = 1
        const val FAST_REWIND_SELECTOR = -1
        const val STOP_SERVICE = "stop_foreground_service"
        const val TRACK_TITLE = "track_title"
        const val TRACK_ID = "track_id"
        const val TRACK_ARTIST = "track_title"
        const val IMAGE_URL = "image_url"
    }
}
