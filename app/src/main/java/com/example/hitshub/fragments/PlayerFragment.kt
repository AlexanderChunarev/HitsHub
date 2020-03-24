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
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hitshub.R
import com.example.hitshub.extentions.changeSpinnerDialogState
import com.example.hitshub.extentions.setBottomNavigationViewVisibility
import com.example.hitshub.extentions.showSupportActionBar
import com.example.hitshub.media.Player
import com.example.hitshub.media.Player.Companion.FAST_FORWARD
import com.example.hitshub.media.Player.Companion.FAST_REWIND
import com.example.hitshub.media.Player.Companion.PAUSE
import com.example.hitshub.media.Player.Companion.PLAY
import com.example.hitshub.media.Player.Companion.TRACK_INTENT
import com.example.hitshub.models.ITrack
import com.example.hitshub.receivers.NotificationBroadcastReceiver
import com.example.hitshub.receivers.NotificationBroadcastReceiver.Companion.RECEIVE_FAST_FORWARD_ACTION_KEY
import com.example.hitshub.receivers.NotificationBroadcastReceiver.Companion.RECEIVE_FAST_REWIND_ACTION_KEY
import com.example.hitshub.receivers.NotificationBroadcastReceiver.Companion.RECEIVE_PAUSE_ACTION_KEY
import com.example.hitshub.services.MediaPlayerService
import com.example.hitshub.utils.NotificationHelper
import com.example.hitshub.viewmodels.DeezerViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_player.*
import java.util.concurrent.TimeUnit

class PlayerFragment : Fragment() {
    private lateinit var track: ITrack
    private lateinit var tracks: MutableList<ITrack>
    private val notificationHelper by lazy { NotificationHelper.getInstance(activity!!.applicationContext) }
    private val player by lazy { Player.getInstance() }
    private val handler by lazy { Handler() }
    private lateinit var runnable: Runnable
    private val viewModel by lazy {
        ViewModelProvider(activity!!).get(DeezerViewModel::class.java)
    }
    private val serviceIntent by lazy { Intent(activity, MediaPlayerService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            track = arguments!!.getSerializable(RESPONSE_KEY) as ITrack
        }
        startMediaPlayerService()
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
        updateUi()
        initializeSeekBar()

        viewModel.topTrackLiveData.observe(viewLifecycleOwner, Observer {
            tracks = it.data.toMutableList()
        })

        player.showSpinner.observe(viewLifecycleOwner, Observer {
            activity!!.supportFragmentManager.changeSpinnerDialogState(it)
        })

        play_button_or_pause_button.setOnClickListener {
            setPlayerState()
            notificationHelper.updateNotification(track, player)
        }

        fast_forward_button.setOnClickListener {
            selectTrack(FAST_FORWARD_SELECTOR)
        }
        fast_rewind_button.setOnClickListener {
            selectTrack(FAST_REWIND_SELECTOR)
        }
    }

    private fun selectTrack(selector: Int) {
        when (selector) {
            FAST_FORWARD_SELECTOR -> {
                (tracks.indexOf(track) + selector).run {
                    if (this < tracks.size) {
                        stopService()
                        track = tracks[this]
                        startMediaPlayerService()
                        updateUi()
                    }
                }
            }
            FAST_REWIND_SELECTOR -> {
                (tracks.indexOf(track) + selector).run {
                    if (this >= 0) {
                        stopService()
                        track = tracks[this]
                        startMediaPlayerService()
                        updateUi()
                    }
                }
            }
        }
    }

    private fun setPlayerState() {
        if (player.isPlaying) {
            player.pause()
            play_button_or_pause_button.setImageResource(R.drawable.ic_play)
        } else {
            player.start()
            play_button_or_pause_button.setImageResource(R.drawable.ic_pause)
        }
    }

    private fun updateUi() {
        track_title_text_view.text = track.title
        track_author_text_view.text = track.artist!!.name
        Picasso.get().load(track.artist!!.pictureBig).into(cover_big_image_view)
        play_button_or_pause_button.setImageResource(R.drawable.ic_pause)
    }

    private fun handleSeekBarPosition() {
        runnable = Runnable {
            if (seekBar != null) {
                seekBar.progress =
                    TimeUnit.MILLISECONDS.toSeconds(player.currentPosition.toLong())
                        .toInt()
                handler.postDelayed(runnable, DELAY)
            }
        }
        handler.postDelayed(runnable, DELAY)
    }

    private fun initializeSeekBar() {
        player.getTrackDuration().observe(viewLifecycleOwner, Observer {
            seekBar.max = it.toInt()
        })
        handleSeekBarPosition()
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    player.seekTo(i * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    override fun onResume() {
        super.onResume()
        activity!!.apply {
            showSupportActionBar(View.GONE)
            setBottomNavigationViewVisibility(View.GONE)
            applicationContext.registerReceiver(
                receiver(),
                IntentFilter(PlayerFragment::class.java.toString())
            )
        }
    }

    private fun receiver() = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when {
                intent?.getStringExtra(RECEIVE_PAUSE_ACTION_KEY) == PAUSE -> {
                    play_button_or_pause_button.setImageResource(R.drawable.ic_play)
                }
                intent?.getStringExtra(NotificationBroadcastReceiver.RECEIVE_PLAY_ACTION_KEY) == PLAY -> {
                    play_button_or_pause_button.setImageResource(R.drawable.ic_pause)
                }
                intent?.getStringExtra(RECEIVE_FAST_FORWARD_ACTION_KEY) == FAST_FORWARD -> {
                    selectTrack(FAST_FORWARD_SELECTOR)
                }
                intent?.getStringExtra(RECEIVE_FAST_REWIND_ACTION_KEY) == FAST_REWIND -> {
                    selectTrack(FAST_REWIND_SELECTOR)
                }
            }
        }
    }

    private fun stopService() = activity!!.stopService(serviceIntent)

    private fun startMediaPlayerService() {
        serviceIntent.putExtra(TRACK_INTENT, track)
        ContextCompat.startForegroundService(activity!!.applicationContext, serviceIntent)
    }

    override fun onStop() {
        super.onStop()
        activity!!.apply {
            showSupportActionBar(View.VISIBLE)
            setBottomNavigationViewVisibility(View.VISIBLE)
        }
    }

    companion object {
        const val FAST_FORWARD_SELECTOR = 1
        const val FAST_REWIND_SELECTOR = -1
        const val RESPONSE_KEY = "response_key"
        const val DELAY = 1000.toLong()
    }
}
