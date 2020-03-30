package com.example.hitshub.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.hitshub.listener.OnItemListener
import com.example.hitshub.media.Player
import com.example.hitshub.services.MediaPlayerService
import kotlinx.android.synthetic.main.fragment_home.*

abstract class BaseFragment : Fragment(), OnItemListener {
    val navController by lazy { NavHostFragment.findNavController(this) }
    protected val serviceIntent by lazy { Intent(activity, MediaPlayerService::class.java) }
    protected val player by lazy { Player.getInstance() }
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
}
