package com.example.hitshub.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hitshub.R
import com.example.hitshub.media.Player
import com.example.hitshub.media.Player.Companion.ACTION_FAST_FORWARD
import com.example.hitshub.media.Player.Companion.ACTION_FAST_REWIND
import com.example.hitshub.media.Player.Companion.ACTION_PAUSE
import com.example.hitshub.media.Player.Companion.ACTION_PLAY
import com.example.hitshub.models.ITrack
import com.example.hitshub.receivers.NotificationBroadcastReceiver
import com.example.hitshub.services.MediaPlayerService
import com.example.hitshub.utils.NotificationHelper
import com.example.hitshub.viewmodels.DeezerViewModel
import kotlinx.android.synthetic.main.fragment_mini_player.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MiniPlayerFragment : Fragment() {
    private lateinit var track: ITrack
    private lateinit var tracks: MutableList<ITrack>
    private val player by lazy { Player.getInstance() }
    private val viewModel by lazy {
        ViewModelProvider(activity!!).get(DeezerViewModel::class.java)
    }
    private val notificationHelper by lazy {
        NotificationHelper.getInstance(activity!!.applicationContext)
    }
    private val serviceIntent by lazy { Intent(activity, MediaPlayerService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            track = arguments!!.getSerializable(RESPONSE_KEY) as ITrack
        }
        serviceIntent.putExtra(Player.TRACK_INTENT, track)
        ContextCompat.startForegroundService(activity!!.applicationContext, serviceIntent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mini_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.topTrackLiveData.observe(viewLifecycleOwner, Observer {
            tracks = it.data.toMutableList()
        })
        player.prepareState.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                player.start()
                updateUI()
            }
        })
        action_button.setOnClickListener {
            setPlayerStateOnNotificationAction()
            // activity!!.supportFragmentManager.beginTransaction().hide(this).commit()
        }
    }

    private fun selectTrack(selector: Int) {
        when (selector) {
            PlayerFragment.FAST_FORWARD_SELECTOR -> {
                (tracks.indexOf(track) + selector).run {
                    if (this < tracks.size) {
                        track = tracks[this]
                        resetPlayer()
                    }
                }
            }
            PlayerFragment.FAST_REWIND_SELECTOR -> {
                (tracks.indexOf(track) + selector).run {
                    if (this >= 0) {
                        track = tracks[this]
                        resetPlayer()
                    }
                }
            }
        }
    }

    private fun setPlayerStateOnNotificationAction() {
        player.prepareState.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                player.apply {
                    if (this.isPlaying) {
                        action_button.setImageResource(R.drawable.ic_play)
                        pause()
                    } else {
                        start()
                        action_button.setImageResource(R.drawable.ic_pause)
                    }
                    notificationHelper.updateNotification(track, isPlaying)
                }
            }
        })
    }

    private fun resetPlayer() = GlobalScope.launch {
        if (view != null) {
            withContext(Dispatchers.Main) {
                player._prepareState.value = false
            }
            player.apply {
                prepareMediaPlayer(track.preview)
                start()
            }
            withContext(Dispatchers.Main) {
                player._prepareState.value = true
                updateUI()
            }
        } else {
            player.apply {
                prepareMediaPlayer(track.preview)
                start()
                notificationHelper.updateNotification(track, player.isPlaying)
            }
        }
    }

    private fun updateUI() {
        player.isPlaying.run {
            when (this) {
                true -> {
                    action_button.setImageResource(R.drawable.ic_pause)
                }
                else -> {
                    action_button.setImageResource(R.drawable.ic_play)
                }
            }
            mini_player_title.text = track.title
            notificationHelper.updateNotification(track, this)
        }
    }

    private fun receiver() = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when {
                intent?.getStringExtra(NotificationBroadcastReceiver.RECEIVE_PAUSE_ACTION_KEY) == ACTION_PAUSE -> {
                    // setPlayerStateOnNotificationAction()
                    if (view != null) {
                        action_button.setImageResource(R.drawable.ic_play)
                    }
                }
                intent?.getStringExtra(NotificationBroadcastReceiver.RECEIVE_PLAY_ACTION_KEY) == ACTION_PLAY -> {
                    // setPlayerStateOnNotificationAction()
                    if (view != null) {
                        action_button.setImageResource(R.drawable.ic_pause)
                    }
                }
                intent?.getStringExtra(NotificationBroadcastReceiver.RECEIVE_FAST_FORWARD_ACTION_KEY) == ACTION_FAST_FORWARD -> {
                    selectTrack(PlayerFragment.FAST_FORWARD_SELECTOR)
                }
                intent?.getStringExtra(NotificationBroadcastReceiver.RECEIVE_FAST_REWIND_ACTION_KEY) == ACTION_FAST_REWIND -> {
                    selectTrack(PlayerFragment.FAST_REWIND_SELECTOR)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity!!.apply {
            applicationContext.registerReceiver(
                receiver(),
                IntentFilter(MiniPlayerFragment::class.java.toString())
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        println("DESTROYYYYY")
    }

    companion object {
        fun newInstance(track: ITrack) = MiniPlayerFragment().apply {
            arguments = Bundle().apply {
                putSerializable(RESPONSE_KEY, track)
            }
        }

        private const val RESPONSE_KEY = "response_key"
    }
}
