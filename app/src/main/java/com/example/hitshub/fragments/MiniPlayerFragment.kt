package com.example.hitshub.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.Fragment
import com.example.hitshub.R
import com.example.hitshub.media.Player.Companion.ACTION_PAUSE
import com.example.hitshub.media.Player.Companion.ACTION_PLAY
import com.example.hitshub.media.Player.Companion.ACTION_PREPARE
import com.example.hitshub.receivers.NotificationBroadcastReceiver.Companion.RECEIVE_PAUSE_ACTION_KEY
import com.example.hitshub.receivers.NotificationBroadcastReceiver.Companion.RECEIVE_PLAY_ACTION_KEY
import com.example.hitshub.receivers.NotificationBroadcastReceiver.Companion.RECEIVE_PREPARE_ACTION_KEY
import com.example.hitshub.services.MediaPlayerService
import com.example.hitshub.services.MediaPlayerService.Companion.TRACK_TITLE
import kotlinx.android.synthetic.main.fragment_mini_player.*

class MiniPlayerFragment : Fragment() {
    private val serviceIntent by lazy { Intent(context, MediaPlayerService::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mini_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        play_pause_button.setOnClickListener {
            when (play_pause_button.tag) {
                ACTION_PAUSE -> {
                    updateControlButtons(ACTION_PAUSE)
                    serviceIntent.action = ACTION_PAUSE
                    startForegroundService(activity!!.applicationContext, serviceIntent)
                }
                ACTION_PLAY -> {
                    updateControlButtons(ACTION_PLAY)
                    serviceIntent.action = ACTION_PLAY
                    startForegroundService(activity!!.applicationContext, serviceIntent)
                }
            }
        }
    }

    fun updateControlButtons(action: String) {
        when (action) {
            ACTION_PAUSE -> {
                play_pause_button.apply {
                    activity!!.findViewById<ImageButton>(R.id.play_button_or_pause)
                        .setImageResource(R.drawable.ic_play)
                    setImageResource(R.drawable.ic_play)
                    tag = ACTION_PLAY
                }
            }
            ACTION_PLAY -> play_pause_button.apply {
                activity!!.findViewById<ImageButton>(R.id.play_button_or_pause)
                    .setImageResource(R.drawable.ic_pause)
                setImageResource(R.drawable.ic_pause)
                tag = ACTION_PAUSE
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when {
                intent?.getStringExtra(RECEIVE_PAUSE_ACTION_KEY) == ACTION_PAUSE -> {
                    if (view != null) {
                        updateControlButtons(ACTION_PAUSE)
                        play_pause_button.tag = ACTION_PLAY
                    }
                }
                intent?.getStringExtra(RECEIVE_PLAY_ACTION_KEY) == ACTION_PLAY -> {
                    if (view != null) {
                        updateControlButtons(ACTION_PLAY)
                        play_pause_button.tag = ACTION_PAUSE
                    }
                }
                intent?.getStringExtra(RECEIVE_PREPARE_ACTION_KEY) == ACTION_PREPARE -> {
                    if (view != null) {
                        play_pause_button.tag = ACTION_PAUSE
                        title_text_mini_player.text = intent.getStringExtra(TRACK_TITLE)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        activity!!.apply {
            applicationContext.registerReceiver(
                receiver,
                IntentFilter(BaseMediaFragment::class.java.toString())
            )
        }
    }
}
