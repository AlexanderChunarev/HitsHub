package com.example.hitshub.fragments

import android.content.Intent
import android.widget.LinearLayout
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat.startForegroundService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.hitshub.R
import com.example.hitshub.listener.OnItemListener
import com.example.hitshub.media.Player.Companion.ACTION_PREPARE
import com.example.hitshub.media.Player.Companion.TRACK_INTENT
import com.example.hitshub.models.ITrack
import com.example.hitshub.services.MediaPlayerService
import java.util.*

abstract class BaseFragment : Fragment(), OnItemListener {
    val navController by lazy { NavHostFragment.findNavController(this) }
    private val serviceIntent by lazy { Intent(activity, MediaPlayerService::class.java) }
    val motionLayout by lazy { activity!!.findViewById<MotionLayout>(R.id.motion_base) }
    abstract val adapter: Adapter<ViewHolder>

    fun callMediaPlayer(currTrack: ITrack, playlist: ArrayList<ITrack>) {
        if (!activity!!.findViewById<LinearLayout>(R.id.mini).isVisible) {
            motionLayout.transitionToEnd()
        }
        serviceIntent.apply {
            action = ACTION_PREPARE
            putExtra(TRACK_INTENT, currTrack)
            putParcelableArrayListExtra("playlist", playlist)
        }
        startForegroundService(activity!!.applicationContext, serviceIntent)
    }

    companion object {
        const val TRANSFER_KEY = "curr_track"
        const val PLAYER_PREVIEW_DURATION = 30
    }
}
