package com.example.hitshub.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.hitshub.R
import com.example.hitshub.extentions.setBottomNavigationViewVisibility
import com.example.hitshub.extentions.showSupportActionBar
import com.example.hitshub.media.Player
import com.example.hitshub.media.Player.Companion.ACTION_FAST_FORWARD
import com.example.hitshub.media.Player.Companion.ACTION_FAST_REWIND
import com.example.hitshub.media.Player.Companion.ACTION_PAUSE
import com.example.hitshub.media.Player.Companion.ACTION_PLAY
import com.example.hitshub.receivers.NotificationBroadcastReceiver
import com.example.hitshub.services.MediaPlayerService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_player.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class PlayerFragment : Fragment() {
    private val player by lazy { Player.getInstance() }
    private val handler by lazy { Handler() }
    private lateinit var runnable: Runnable
    private val serviceIntent by lazy { Intent(activity, MediaPlayerService::class.java) }
    private val navController by lazy { NavHostFragment.findNavController(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player, container, false)
        view.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_MOVE) {
                Toast.makeText(activity!!.applicationContext, "DOWN", Toast.LENGTH_LONG).show()
            }
            return@setOnTouchListener true
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeSeekBar()
//        player.setOnCompletionListener {
//            player.next(FAST_FORWARD_SELECTOR)
//            startForegroundService()
//        }
        player.prepareState.observe(viewLifecycleOwner, Observer {
            if (it) {
                updateUI()
                play_button_or_pause_button.setOnClickListener {
                    player.apply {
                        if (this.isPlaying) {
                            pause()
                            updateControlButtons(ACTION_PAUSE)
                        } else {
                            start()
                            updateControlButtons(ACTION_PLAY)
                        }
                    }
                }
                fast_forward_button.setOnClickListener {
                    player.next(FAST_FORWARD_SELECTOR)
                    startForegroundService()
                }
                fast_rewind_button.setOnClickListener {
                    player.next(FAST_REWIND_SELECTOR)
                    startForegroundService()
                }
            }
        })
        chat_image_button.setOnClickListener {
            navController.navigate(R.id.chatFragment)
        }
//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                //(activity!!.findViewById(R.id.motion_item_activity) as MotionLayout) .transitionToStart()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun updateUI() {
        player.isPlaying.run {
            when (this) {
                true -> {
                    play_button_or_pause_button.setImageResource(R.drawable.ic_pause)
                }
                else -> {
                    play_button_or_pause_button.setImageResource(R.drawable.ic_play)
                }
            }
            player.track.apply {
                track_title_text_view.text = this.title
                track_author_text_view.text = this.artist!!.name
                Picasso.get().load(this.artist!!.pictureBig).into(cover_big_image_view)
                play_button_or_pause_button.setImageResource(R.drawable.ic_pause)
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
        seekBar.max = PLAYER_PREVIEW_DURATION
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

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when {
                intent?.getStringExtra(NotificationBroadcastReceiver.RECEIVE_PAUSE_ACTION_KEY) == ACTION_PAUSE -> {
                    updateControlButtons(ACTION_PAUSE)
                }
                intent?.getStringExtra(NotificationBroadcastReceiver.RECEIVE_PLAY_ACTION_KEY) == ACTION_PLAY -> {
                    updateControlButtons(ACTION_PLAY)
                }
                intent?.getStringExtra(NotificationBroadcastReceiver.RECEIVE_FAST_FORWARD_ACTION_KEY) == ACTION_FAST_FORWARD -> {
                    startForegroundService()
                }
                intent?.getStringExtra(NotificationBroadcastReceiver.RECEIVE_FAST_REWIND_ACTION_KEY) == ACTION_FAST_REWIND -> {
                    startForegroundService()
                }
            }
        }
    }

    private fun startForegroundService() = GlobalScope.launch {
        if (view != null) {
            withContext(Dispatchers.Main) {
                player._prepareState.value = false
            }
            withContext(Dispatchers.IO) {
                serviceIntent.putExtra(Player.TRACK_INTENT, player.track)
                startForegroundService(activity!!.applicationContext, serviceIntent)
            }
            withContext(Dispatchers.Main) {
                player._prepareState.value = true
                updateUI()
            }
        } else {
            player.apply {
                prepareMediaPlayer()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity!!.apply {
            applicationContext.registerReceiver(
                receiver,
                IntentFilter(PlayerFragment::class.java.toString())
            )
            showSupportActionBar(View.GONE)
            setBottomNavigationViewVisibility(View.GONE)
        }
    }

    override fun onStop() {
        super.onStop()
        activity!!.apply {
            showSupportActionBar(View.VISIBLE)
            setBottomNavigationViewVisibility(View.VISIBLE)
        }
    }

    companion object {
        const val TRANSFER_KEY = "curr_track"
        const val FAST_FORWARD_SELECTOR = 1
        const val FAST_REWIND_SELECTOR = -1
        const val DELAY = 1000.toLong()
        const val PLAYER_PREVIEW_DURATION = 30
    }
}
