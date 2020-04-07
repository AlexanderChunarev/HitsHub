package com.example.hitshub.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.hitshub.R
import com.example.hitshub.listener.OnItemListener
import com.example.hitshub.media.Player
import com.example.hitshub.models.ITrack
import com.example.hitshub.services.MediaPlayerService
import java.util.*

abstract class BaseFragment : Fragment(), OnItemListener {
    val navController by lazy { NavHostFragment.findNavController(this) }
    private val serviceIntent by lazy { Intent(activity, MediaPlayerService::class.java) }
    val motionLayout by lazy { activity!!.findViewById<MotionLayout>(R.id.motion_base) }
    abstract val adapter: Adapter<ViewHolder>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity!!.findViewById<FrameLayout>(R.id.mini_player_container).setOnClickListener {
            motionLayout.transitionToEnd()
        }
    }

    fun callMediaPlayer(currTrack: ITrack, playlist: ArrayList<ITrack>) {
        activity!!.supportFragmentManager.apply {
            if (findFragmentByTag(MiniPlayerFragment::class.java.toString()) == null) {
                beginTransaction().replace(
                    R.id.mini_player_container,
                    MiniPlayerFragment(),
                    MiniPlayerFragment::class.java.toString()
                ).commit()
            }
        }
        serviceIntent.apply {
            action = Player.ACTION_PREPARE
            putExtra(Player.TRACK_INTENT, currTrack)
            putParcelableArrayListExtra("playlist", playlist)
        }
        startForegroundService(activity!!.applicationContext, serviceIntent)
    }
}
