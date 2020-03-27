package com.example.hitshub.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.hitshub.R
import com.example.hitshub.activities.MainActivity
import com.example.hitshub.fragments.PlayerFragment.Companion.TRANSFER_KEY
import com.example.hitshub.media.Player
import com.example.hitshub.models.IAlbum
import com.example.hitshub.models.ITrack
import com.example.hitshub.models.VerticalModel
import com.example.hitshub.services.MediaPlayerService

class HomeFragment : BaseMediaFragment() {
    override val arrayListVertical by lazy { mutableListOf<VerticalModel>() }
    private val serviceIntent by lazy { Intent(activity, MediaPlayerService::class.java) }
    private val navController by lazy { NavHostFragment.findNavController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity!!.intent.action.apply {
            if (this == MainActivity.OPEN_PLAYER_FRAGMENT) {
                navController.navigate(R.id.player_fragment)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onClickItem(response: ITrack) {
        serviceIntent.putExtra(Player.TRACK_INTENT, response)
        ContextCompat.startForegroundService(activity!!.applicationContext, serviceIntent)
        navController.navigate(R.id.player_fragment, Bundle().apply {
            putSerializable(TRANSFER_KEY, response)
        })
    }

    override fun onClickItem(response: IAlbum) {
        Toast.makeText(
            activity!!.applicationContext,
            "temporarily click on album unavailable. Only click on track to listen",
            Toast.LENGTH_LONG
        ).show()
    }
}
