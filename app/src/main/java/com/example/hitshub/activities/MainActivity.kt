package com.example.hitshub.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat.startForegroundService
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hitshub.R
import com.example.hitshub.activities.BaseActivity.Companion.USER
import com.example.hitshub.activities.ChatActivity.Companion.TRACK_ID_KEY
import com.example.hitshub.activities.ChatActivity.Companion.USER_KEY
import com.example.hitshub.activities.ChatActivity.Companion.WRITE_MESSAGE_AT
import com.example.hitshub.fragments.BaseFragment.Companion.PLAYER_PREVIEW_DURATION
import com.example.hitshub.fragments.BaseMediaFragment
import com.example.hitshub.media.Player
import com.example.hitshub.media.Player.Companion.ACTION_PAUSE
import com.example.hitshub.media.Player.Companion.ACTION_PLAY
import com.example.hitshub.models.FragmentState
import com.example.hitshub.models.User
import com.example.hitshub.receivers.NotificationBroadcastReceiver
import com.example.hitshub.services.MediaPlayerService
import com.example.hitshub.utils.Constants.EMPTY_STRING
import com.example.hitshub.utils.InjectorUtils
import com.example.hitshub.utils.MessageSelector
import com.example.hitshub.utils.MessageSelector.Companion.PAUSE
import com.example.hitshub.viewmodels.DeezerViewModel
import com.example.hitshub.viewmodels.FirebaseDatabaseViewModel
import com.example.hitshub.viewmodels.FragmentStateViewModel
import com.example.hitshub.viewmodels.MediaViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(DeezerViewModel::class.java)
    }
    private val factory by lazy { InjectorUtils.provideViewModelFactory(this) }
    private val mediaViewModel by lazy {
        ViewModelProvider(this, factory).get(MediaViewModel::class.java)
    }
    private val user by lazy { intent.getParcelableExtra<User>(USER) }
    private val fragmentStateViewModel: FragmentStateViewModel by viewModels()
    private val firebaseViewModel: FirebaseDatabaseViewModel by viewModels()
    private val serviceIntent by lazy { Intent(this, MediaPlayerService::class.java) }
    private val motionLayout by lazy { findViewById<MotionLayout>(R.id.motion_base) }
    private val player by lazy { Player.getInstance() }
    private val messageSelector by lazy { MessageSelector.getInstance() }
    private var fragmentState: FragmentState? = null
    private var resetTime: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_search, R.id.navigation_profile
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        getMessages()
        setBackPressCallback()
        initializeSeekBar()
        play_pause_button.setListener()
        play_button_or_pause.setListener()
        fast_forward_button.setOnClickListener {
            serviceIntent.action = Player.ACTION_SKIP_NEXT
            startForegroundService(this, serviceIntent)
        }
        fast_rewind_button.setOnClickListener {
            serviceIntent.action = Player.ACTION_SKIP_PREV
            startForegroundService(this, serviceIntent)
        }
        favourite_image_button.setOnClickListener {
            if (favourite_image_button.tag == LIKED) {
                favourite_image_button.tag = UNLIKE
                favourite_image_button.setImageResource(R.drawable.ic_favorite_24dp)
                mediaViewModel.deleteTrack(player.currentTrack.id.toString())
            } else {
                favourite_image_button.tag = LIKED
                favourite_image_button.setImageResource(R.drawable.ic_favorite_pressed)
                mediaViewModel.insertTrack(player.currentTrack)
            }
        }
        chat_image_button.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java).apply {
                putExtra(TRACK_ID_KEY, player.currentTrack.id)
                putExtra(WRITE_MESSAGE_AT, seekBar.progress)
                putExtra(USER_KEY, user)
            })
        }

        intent.action.apply {
            if (this == WAKE_UP_MEDIA_PLAYER) {
                serviceIntent.action = WAKE_UP_MEDIA_PLAYER
                startForegroundService(this@MainActivity, serviceIntent)
            }
        }
        viewModel.apply {
            getTopTracks()
            getTopAlbums()
        }
    }

    private fun setFavouriteStatus() {
        mediaViewModel.apply {
            selectTrackById(player.currentTrack.id.toString())
            trackLiveData.observe(this@MainActivity, Observer {
                if (it != null) {
                    favourite_image_button.tag = LIKED
                    favourite_image_button.setImageResource(R.drawable.ic_favorite_pressed)
                } else {
                    favourite_image_button.tag = UNLIKE
                    favourite_image_button.setImageResource(R.drawable.ic_favorite_24dp)
                }
            })
        }
    }

    private fun getMessages() {
        firebaseViewModel.apply {
            messages.observe(this@MainActivity, Observer {
                messageSelector.setMessages(it.filter { message -> message.trackId == player.currentTrack.id })
                messageSelector.get(
                    playerCurrPos = 1,
                    duration = PLAYER_PREVIEW_DURATION
                )
            })
        }
    }

    private fun setBackPressCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!mini.isVisible) {
                    motionLayout.transitionToStart()
                } else {
                    finish()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun updateControlButtons() {
        when (player.isPlaying) {
            false -> {
                play_button_or_pause.apply {
                    setImageResource(R.drawable.ic_play)
                    tag = ACTION_PLAY
                }
                play_pause_button.apply {
                    setImageResource(R.drawable.ic_play)
                    tag = ACTION_PLAY
                }
            }
            true -> {
                play_button_or_pause.apply {
                    setImageResource(R.drawable.ic_pause)
                    tag = ACTION_PAUSE
                }
                play_pause_button.apply {
                    setImageResource(R.drawable.ic_pause)
                    tag = ACTION_PAUSE
                }
            }
        }
    }

    fun saveFragmentState(intent: Intent) = FragmentState().apply {
        with(intent) {
            trackId = getLongExtra(MediaPlayerService.TRACK_ID, -1)
            trackAuthor = getStringExtra(MediaPlayerService.TRACK_ARTIST)!!
            trackTitle = getStringExtra(MediaPlayerService.TRACK_TITLE)!!
            imageUrl = getStringExtra(MediaPlayerService.IMAGE_URL)!!
        }
        tag = if (play_button_or_pause == null) {
            ACTION_PAUSE
        } else {
            play_button_or_pause.tag.toString()
        }
    }

    private fun update() {
        handleSeekBarPosition()
        updateControlButtons()
        fragmentState!!.apply {
            track_author_text_view.text = trackAuthor
            track_title_text_view_1.text = trackTitle
            title_text_mini_player.text = trackTitle
            Picasso.get().load(imageUrl).into(cover_big_image_view)
        }
    }

    private fun handleSeekBarPosition() = GlobalScope.launch {
        withContext(Dispatchers.IO) {
            try {
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
            } catch (e: IllegalStateException) {
            }
        }
    }

    fun selectMessage(time: Int) {
        messageSelector.list.forEach {
            if (it.time == time) {
                it.apply {
                    chat_comment_textView.text = content
                    Picasso.get().load(avatarUrl).fit().into(small_avatar_imageView)
                    resetTime = it.time + PAUSE
                }
            }
        }
    }

    private fun reset() {
        chat_comment_textView.text = EMPTY_STRING
        small_avatar_imageView.setImageResource(0)
    }

    private fun initializeSeekBar() {
        seekBar.max = PLAYER_PREVIEW_DURATION
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                selectMessage(i)
                if (i == resetTime) {
                    reset()
                }
                if (b) {
                    messageSelector.get(
                        playerCurrPos = i,
                        duration = PLAYER_PREVIEW_DURATION
                    )
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
                    updateControlButtons()
                    play_button_or_pause.tag = ACTION_PLAY
                    play_pause_button.tag = ACTION_PLAY
                }
                intent?.getStringExtra(NotificationBroadcastReceiver.RECEIVE_PLAY_ACTION_KEY) == ACTION_PLAY -> {
                    updateControlButtons()
                    play_button_or_pause.tag = ACTION_PAUSE
                    play_pause_button.tag = ACTION_PAUSE
                }
                intent?.getStringExtra(NotificationBroadcastReceiver.RECEIVE_PREPARE_ACTION_KEY) == Player.ACTION_PREPARE -> {
                    firebaseViewModel.getMessages(player.currentTrack.id)
                    play_button_or_pause.tag = ACTION_PAUSE
                    play_pause_button.tag = ACTION_PAUSE
                    fragmentState = saveFragmentState(intent)
                    fragmentStateViewModel.save(fragmentState!!)
                    setFavouriteStatus()
                    update()
                }
            }
        }
    }

    private fun ImageButton.setListener() {
        this.setOnClickListener {
            when (this.tag) {
                ACTION_PAUSE -> {
                    serviceIntent.action = ACTION_PAUSE
                    startForegroundService(
                        this@MainActivity,
                        serviceIntent
                    )
                }
                ACTION_PLAY -> {
                    serviceIntent.action = ACTION_PLAY
                    startForegroundService(
                        this@MainActivity,
                        serviceIntent
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(
            receiver,
            IntentFilter(BaseMediaFragment::class.java.toString())
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    companion object {
        const val WAKE_UP_MEDIA_PLAYER = "open_player_from_notification"
        const val LIKED = "liked"
        const val UNLIKE = "unlike"
    }
}
