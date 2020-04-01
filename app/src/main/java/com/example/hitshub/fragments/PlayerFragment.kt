package com.example.hitshub.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat.startForegroundService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hitshub.R
import com.example.hitshub.activities.BaseActivity
import com.example.hitshub.adapter.MessageRecyclerViewAdapter
import com.example.hitshub.media.Player
import com.example.hitshub.media.Player.Companion.ACTION_PAUSE
import com.example.hitshub.media.Player.Companion.ACTION_PLAY
import com.example.hitshub.media.Player.Companion.ACTION_SKIP_NEXT
import com.example.hitshub.media.Player.Companion.ACTION_SKIP_PREV
import com.example.hitshub.models.Message
import com.example.hitshub.models.User
import com.example.hitshub.receivers.NotificationBroadcastReceiver.Companion.RECEIVE_PAUSE_ACTION_KEY
import com.example.hitshub.receivers.NotificationBroadcastReceiver.Companion.RECEIVE_PLAY_ACTION_KEY
import com.example.hitshub.receivers.NotificationBroadcastReceiver.Companion.RECEIVE_PREPARE_ACTION_KEY
import com.example.hitshub.services.MediaPlayerService
import com.example.hitshub.services.MediaPlayerService.Companion.IMAGE_URL
import com.example.hitshub.services.MediaPlayerService.Companion.TRACK_ARTIST
import com.example.hitshub.services.MediaPlayerService.Companion.TRACK_ID
import com.example.hitshub.services.MediaPlayerService.Companion.TRACK_TITLE
import com.example.hitshub.utils.Constants.EMPTY_STRING
import com.example.hitshub.viewmodels.FirebaseDatabaseViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main_end.*
import kotlinx.android.synthetic.main.fragment_player.*
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class PlayerFragment : Fragment() {
    private val serviceIntent by lazy { Intent(activity, MediaPlayerService::class.java) }
    private val firebaseViewModel by lazy { FirebaseDatabaseViewModel() }
    private val user by lazy { activity!!.intent.getSerializableExtra(BaseActivity.USER) as User }
    private val adapter by lazy {
        MessageRecyclerViewAdapter()
    }
    private val player by lazy { Player.getInstance() }
    private var trackID: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeSeekBar()
        with(chat_recycler_view) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity!!.applicationContext)
            adapter = this@PlayerFragment.adapter
        }
        firebaseViewModel.messages.observe(viewLifecycleOwner, Observer {
            adapter.addAll(it)
        })

        send_message_button.setOnClickListener {
            if (message_editText.text.isNotEmpty()) {
                Message(
                    name = user.name,
                    avatarUrl = user.avatarUrl,
                    content = message_editText.text.toString(),
                    trackId = trackID!!,
                    time = TimeUnit.MILLISECONDS.toSeconds(20.toLong()).toInt()
                ).apply {
                    adapter.addItem(this)
                    firebaseViewModel.push(this)
                }
            }
        }

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
        fast_forward_button.setOnClickListener {
            resetChatMessage()
            serviceIntent.action = ACTION_SKIP_NEXT
            startForegroundService(activity!!.applicationContext, serviceIntent)
        }
        fast_rewind_button.setOnClickListener {
            resetChatMessage()
            serviceIntent.action = ACTION_SKIP_PREV
            startForegroundService(activity!!.applicationContext, serviceIntent)
        }
        chat_image_button.setOnClickListener {
            (activity!!.findViewById(R.id.fagment_player_lay) as MotionLayout).transitionToEnd()
        }
    }

    private fun resetChatMessage() {
        chat_comment_textView.text = EMPTY_STRING
        small_avatar_imageView.setImageResource(0)
    }

    private fun update(intent: Intent) {
        trackID = intent.getLongExtra(TRACK_ID, -1)
        trackID?.let {
            adapter.clear()
            firebaseViewModel.getMessages(it.toString())
        }
        if (view != null) {
            intent.apply {
                track_title_text_view.text = getStringExtra(TRACK_TITLE)
                track_author_text_view.text = getStringExtra(TRACK_ARTIST)
                Picasso.get().load(getStringExtra(IMAGE_URL)).fit().into(cover_big_image_view)
            }
        }
    }

    fun updateControlButtons(action: String) {
        if (view != null) {
            when (action) {
                ACTION_PAUSE -> play_button_or_pause.apply {
                    activity!!.findViewById<ImageButton>(R.id.play_pause_button)
                        .setImageResource(R.drawable.ic_play)
                    setImageResource(R.drawable.ic_play)
                    tag = ACTION_PLAY
                }
                ACTION_PLAY -> play_button_or_pause.apply {
                    activity!!.findViewById<ImageButton>(R.id.play_pause_button)
                        .setImageResource(R.drawable.ic_pause)
                    setImageResource(R.drawable.ic_pause)
                    tag = ACTION_PAUSE
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
        resetChatMessage()
        adapter.messages.getMessage(time)?.let {
            chat_comment_textView.text = it.content
            Picasso.get().load(it.avatarUrl).fit().into(small_avatar_imageView)
        }
    }

    private suspend fun handleSeekBarPosition() = withContext(Dispatchers.IO) {
        while (player.isPlaying) {
            withContext(Dispatchers.Main) {
                if (activity!!.findViewById<FrameLayout>(R.id.player_container).isVisible) {
                    seekBar.progress =
                        TimeUnit.MILLISECONDS.toSeconds(player.currentPosition.toLong())
                            .toInt()
                }
            }
            delay(1000)
        }
    }

    private fun initializeSeekBar() {
        seekBar.max = PLAYER_PREVIEW_DURATION
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                selectMessage(i)
                if (i == PLAYER_PREVIEW_DURATION) {
                    seekBar.progress = 0
                    resetChatMessage()
                }
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
                intent?.getStringExtra(RECEIVE_PREPARE_ACTION_KEY) == Player.ACTION_PREPARE -> {
                    if (view != null) {
                        GlobalScope.launch {
                            handleSeekBarPosition()
                        }
                        play_button_or_pause.tag = ACTION_PAUSE
                        update(intent)
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
        const val SEEK_TO_POSITION = "seek_position"
    }
}
