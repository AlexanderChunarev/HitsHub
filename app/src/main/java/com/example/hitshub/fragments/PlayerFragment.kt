package com.example.hitshub.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.hitshub.R
import com.example.hitshub.media.Player
import com.example.hitshub.media.Player.Companion.TRACK_URL
import com.example.hitshub.models.ITrack
import com.example.hitshub.services.MediaPlayerService
import kotlinx.android.synthetic.main.fragment_player.*
import java.util.concurrent.TimeUnit

class PlayerFragment : Fragment() {
    private lateinit var track: ITrack
    private val player by lazy { Player.getInstance() }
    private val handler by lazy { Handler() }
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            track = arguments!!.getSerializable(RESPONSE_KEY) as ITrack
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

        track_title_text_view.text = track.title
        track_author_text_view.text = track.artist!!.name

        startMediaPlayerService()
        initializeSeekBar()
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

        play_button_or_pause_button.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
                play_button_or_pause_button.setImageResource(R.drawable.ic_play)
            } else {
                player.start()
                play_button_or_pause_button.setImageResource(R.drawable.ic_pause)
            }
        }

        fast_forward_button.setOnClickListener {
        }

        fast_rewind_button.setOnClickListener {
        }
    }

    private fun startMediaPlayerService() {
        val intent = Intent(activity, MediaPlayerService().javaClass)
        intent.putExtra(TRACK_URL, track.preview)
        ContextCompat.startForegroundService(activity!!.applicationContext, intent)
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
    }

    companion object {
        fun newInstance(track: ITrack) = PlayerFragment().apply {
            arguments = Bundle().apply {
                putSerializable(RESPONSE_KEY, track)
            }
        }

        const val RESPONSE_KEY = "response_key"
        const val DELAY = 1000.toLong()
    }
}
