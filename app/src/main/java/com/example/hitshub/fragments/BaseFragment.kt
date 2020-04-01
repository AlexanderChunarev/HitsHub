package com.example.hitshub.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.hitshub.R
import com.example.hitshub.listener.OnItemListener
import com.example.hitshub.media.Player
import com.example.hitshub.models.ITrack
import com.example.hitshub.services.MediaPlayerService
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

abstract class BaseFragment : Fragment(), OnItemListener {
    val navController by lazy { NavHostFragment.findNavController(this) }
    private val serviceIntent by lazy { Intent(activity, MediaPlayerService::class.java) }
    abstract val adapter: Adapter<ViewHolder>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parent_recycler_view.apply {
            layoutManager =
                LinearLayoutManager(
                    activity!!.applicationContext,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = this@BaseFragment.adapter
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
                beginTransaction().replace(
                    R.id.player_container,
                    PlayerFragment(),
                    PlayerFragment::class.java.toString()
                ).commit()
            }
        }
        serviceIntent.apply {
            action = Player.ACTION_PREPARE
            putExtra(Player.TRACK_INTENT, currTrack)
            putParcelableArrayListExtra("playlist", playlist)
        }
        ContextCompat.startForegroundService(activity!!.applicationContext, serviceIntent)
    }
}
