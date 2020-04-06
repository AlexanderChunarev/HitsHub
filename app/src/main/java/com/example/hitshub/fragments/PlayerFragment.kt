package com.example.hitshub.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.hitshub.R
import com.example.hitshub.fragments.ChatFragment.Companion.TRACK_ID_KEY
import com.example.hitshub.fragments.ChatFragment.Companion.WRITE_MESSAGE_AT
import com.example.hitshub.media.Player
import com.example.hitshub.media.Player.Companion.ACTION_PAUSE
import com.example.hitshub.media.Player.Companion.ACTION_PLAY
import com.example.hitshub.media.Player.Companion.ACTION_PREPARE
import com.example.hitshub.media.Player.Companion.ACTION_SKIP_NEXT
import com.example.hitshub.media.Player.Companion.ACTION_SKIP_PREV
import com.example.hitshub.models.FragmentState
import com.example.hitshub.models.Message
import com.example.hitshub.receivers.NotificationBroadcastReceiver.Companion.RECEIVE_PAUSE_ACTION_KEY
import com.example.hitshub.receivers.NotificationBroadcastReceiver.Companion.RECEIVE_PLAY_ACTION_KEY
import com.example.hitshub.receivers.NotificationBroadcastReceiver.Companion.RECEIVE_PREPARE_ACTION_KEY
import com.example.hitshub.services.MediaPlayerService
import com.example.hitshub.services.MediaPlayerService.Companion.IMAGE_URL
import com.example.hitshub.services.MediaPlayerService.Companion.TRACK_ARTIST
import com.example.hitshub.services.MediaPlayerService.Companion.TRACK_ID
import com.example.hitshub.services.MediaPlayerService.Companion.TRACK_TITLE
import com.example.hitshub.utils.Constants.EMPTY_STRING
import com.example.hitshub.viewmodels.FragmentStateViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_player.*
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class PlayerFragment : Fragment() {
    private val fragmentStateViewModel: FragmentStateViewModel by activityViewModels()
    private val navController by lazy { NavHostFragment.findNavController(this) }
    private val serviceIntent by lazy { Intent(activity, MediaPlayerService::class.java) }
    private val motionLayout by lazy { activity!!.findViewById<MotionLayout>(R.id.motion_base) }
    private val player by lazy { Player.getInstance() }
    private var fragmentState: FragmentState? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentStateViewModel.fragmentStateLiveData.observe(viewLifecycleOwner, Observer {
            fragmentState = it
            update()
        })
        initializeSeekBar()
        play_button_or_pause.setOnClickListener {
            when (play_button_or_pause.tag) {
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
        favourite_image_button.setOnClickListener {
        }
        fast_forward_button.setOnClickListener {
            serviceIntent.action = ACTION_SKIP_NEXT
            startForegroundService(activity!!.applicationContext, serviceIntent)
        }
        fast_rewind_button.setOnClickListener {
            reset()
            serviceIntent.action = ACTION_SKIP_PREV
            startForegroundService(activity!!.applicationContext, serviceIntent)
        }
        chat_image_button.setOnClickListener {
            navController.navigate(R.id.chat_fragment, Bundle().apply {
                putLong(TRACK_ID_KEY, fragmentState!!.trackId)
                putLong(WRITE_MESSAGE_AT, player.currentPosition.toLong())
            })
        }
        hide_player_button.setOnClickListener {
            motionLayout.transitionToStart()
        }
    }

    private fun reset() {
        seekBar.progress = 0
        chat_comment_textView.text = EMPTY_STRING
        small_avatar_imageView.setImageResource(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (fragmentState != null) {
            fragmentStateViewModel.save(fragmentState!!)
        }
    }

    private fun update() {
        handleSeekBarPosition()
        view?.let {
            if (player.isPlaying) {
                updateControlButtons(ACTION_PLAY)
            } else {
                updateControlButtons(ACTION_PAUSE)
            }
            fragmentState!!.apply {
                track_title_text_view.text = this.trackTitle
                track_author_text_view.text = this.trackAuthor
                Picasso.get().load(this.imageUrl).fit().into(cover_big_image_view)
            }
        }
    }

    fun saveFragmentState(intent: Intent) = FragmentState().apply {
        with(intent) {
            trackId = getLongExtra(TRACK_ID, -1)
            trackAuthor = getStringExtra(TRACK_ARTIST)!!
            trackTitle = getStringExtra(TRACK_TITLE)!!
            imageUrl = getStringExtra(IMAGE_URL)!!
        }
        if (play_button_or_pause == null) {
            tag = ACTION_PAUSE
        } else {
            tag = play_button_or_pause.tag.toString()
        }
    }

    private fun updateControlButtons(action: String) {
        if (view != null) {
            when (action) {
                ACTION_PAUSE -> {
                    play_button_or_pause.apply {
                        setImageResource(R.drawable.ic_play)
                        tag = ACTION_PLAY
                    }
                }
                ACTION_PLAY -> {
                    play_button_or_pause.apply {
                        setImageResource(R.drawable.ic_pause)
                        tag = ACTION_PAUSE
                    }
                }
            }
        }
    }

    private fun MutableList<Message>.getMessage(time: Int): Message? {
        val sample = run {
            this.filter { it.time == time }.toList()
        }
        return if (sample.isNotEmpty()) sample.random() else null
    }

    fun selectMessage(time: Int) {
//        adapter.messages.getMessage(time)?.let {
//            chat_comment_textView.text = it.content
//            Picasso.get().load(it.avatarUrl).fit().into(small_avatar_imageView)
//        }
    }

    private fun handleSeekBarPosition() = GlobalScope.launch {
        withContext(Dispatchers.IO) {
            while (player.currentPosition != PLAYER_PREVIEW_DURATION) {
                withContext(Dispatchers.Main) {
                    seekBar?.let {
                        it.progress =
                            TimeUnit.MILLISECONDS.toSeconds(player.currentPosition.toLong())
                                .toInt()
                    }
                }
                delay(1000)
            }
        }
    }

    private fun initializeSeekBar() {
        seekBar.max = PLAYER_PREVIEW_DURATION
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                selectMessage(i)
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
                intent?.getStringExtra(RECEIVE_PAUSE_ACTION_KEY) == ACTION_PAUSE -> {
                    if (view != null) {
                        updateControlButtons(ACTION_PAUSE)
                        play_button_or_pause.tag = ACTION_PLAY
                    }
                }
                intent?.getStringExtra(RECEIVE_PLAY_ACTION_KEY) == ACTION_PLAY -> {
                    if (view != null) {
                        updateControlButtons(ACTION_PLAY)
                        play_button_or_pause.tag = ACTION_PAUSE
                    }
                }
                intent?.getStringExtra(RECEIVE_PREPARE_ACTION_KEY) == ACTION_PREPARE -> {
                    if (view != null) {
                        play_button_or_pause.tag = ACTION_PAUSE
                        fragmentState = saveFragmentState(intent)
                    } else {
                        fragmentState = saveFragmentState(intent)
                        fragmentStateViewModel.save(fragmentState!!)
                    }
                    update()
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
        const val PLAYER_PREVIEW_DURATION = 30
    }
}
