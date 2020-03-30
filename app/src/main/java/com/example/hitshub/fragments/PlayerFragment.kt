package com.example.hitshub.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.hitshub.R
import com.example.hitshub.media.Player.Companion.ACTION_PAUSE
import com.example.hitshub.media.Player.Companion.ACTION_PLAY
import com.example.hitshub.media.Player.Companion.ACTION_SKIP_NEXT
import com.example.hitshub.media.Player.Companion.ACTION_SKIP_PREV
import com.example.hitshub.receivers.NotificationBroadcastReceiver.Companion.RECEIVE_PAUSE_ACTION_KEY
import com.example.hitshub.receivers.NotificationBroadcastReceiver.Companion.RECEIVE_PLAY_ACTION_KEY
import com.example.hitshub.services.MediaPlayerService
import com.example.hitshub.services.MediaPlayerService.Companion.IMAGE_URL
import com.example.hitshub.services.MediaPlayerService.Companion.TRACK_ARTIST
import com.example.hitshub.services.MediaPlayerService.Companion.TRACK_TITLE
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_player.*

class PlayerFragment : Fragment() {
    private val handler by lazy { Handler() }
    private lateinit var runnable: Runnable
    private val serviceIntent by lazy { Intent(activity, MediaPlayerService::class.java) }
    private val navController by lazy { NavHostFragment.findNavController(this) }
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            (activity!!.findViewById(R.id.motion_base) as MotionLayout).transitionToStart()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initializeSeekBar()

        play_button_or_pause_button.tag = ACTION_PAUSE
        play_button_or_pause_button.setOnClickListener {
            when (play_button_or_pause_button.tag) {
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
        fast_forward_button.setOnClickListener {
            serviceIntent.action = ACTION_SKIP_NEXT
            startForegroundService(activity!!.applicationContext, serviceIntent)
        }
        fast_rewind_button.setOnClickListener {
            serviceIntent.action = ACTION_SKIP_PREV
            startForegroundService(activity!!.applicationContext, serviceIntent)
        }
        chat_image_button.setOnClickListener {
            navController.navigate(R.id.chatFragment)
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun updateUI(intent: Intent) {
        if (view != null) {
            intent.apply {
                track_title_text_view.text = getStringExtra(TRACK_TITLE)
                track_author_text_view.text = getStringExtra(TRACK_ARTIST)
                Picasso.get().load(getStringExtra(IMAGE_URL)).into(cover_big_image_view)
            }
        }
    }

    fun updateControlButtons(action: String) {
        if (view != null) {
            when (action) {
                ACTION_PAUSE -> play_button_or_pause_button.setImageResource(R.drawable.ic_play)
                ACTION_PLAY -> play_button_or_pause_button.setImageResource(R.drawable.ic_pause)
            }
        }
    }

    private fun handleSeekBarPosition() {
//        runnable = Runnable {
//            if (seekBar != null) {
//                seekBar.progress =
//                    TimeUnit.MILLISECONDS.toSeconds(player.currentPosition.toLong())
//                        .toInt()
//                handler.postDelayed(runnable, DELAY)
//            }
//        }
//        handler.postDelayed(runnable, DELAY)
    }

    private fun initializeSeekBar() {
//        seekBar.max = PLAYER_PREVIEW_DURATION
//        handleSeekBarPosition()
//        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
//                if (b) {
//                    player.seekTo(i * 1000)
//                }
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) {}
//
//            override fun onStopTrackingTouch(seekBar: SeekBar) {}
//        })
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateUI(intent!!)
            when {
                intent.getStringExtra(RECEIVE_PAUSE_ACTION_KEY) == ACTION_PAUSE -> {
                    if (view != null) {
                        updateControlButtons(ACTION_PAUSE)
                        play_button_or_pause_button.tag = ACTION_PLAY
                    }
                }
                intent.getStringExtra(RECEIVE_PLAY_ACTION_KEY) == ACTION_PLAY -> {
                    if (view != null) {
                        updateControlButtons(ACTION_PLAY)
                        play_button_or_pause_button.tag = ACTION_PAUSE
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity!!.apply {
            applicationContext.registerReceiver(
                receiver,
                IntentFilter(BaseMediaFragment::class.java.toString())
            )
        }
    }

    companion object {
        const val TRANSFER_KEY = "curr_track"
        const val DELAY = 1000.toLong()
        const val PLAYER_PREVIEW_DURATION = 30
    }
}
