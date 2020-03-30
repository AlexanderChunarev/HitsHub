package com.example.hitshub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.hitshub.R
import com.example.hitshub.adapter.VerticalRVAdapter
import com.example.hitshub.fragments.PlayerFragment.Companion.TRANSFER_KEY
import com.example.hitshub.media.Player.Companion.ACTION_PLAY
import com.example.hitshub.media.Player.Companion.ACTION_PREPARE
import com.example.hitshub.media.Player.Companion.TRACK_INTENT
import com.example.hitshub.models.IAlbum
import com.example.hitshub.models.ITrack
import com.example.hitshub.models.VerticalModel
import com.example.hitshub.viewmodels.DeezerViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class HomeFragment : BaseFragment() {
    override val adapter by lazy {
        VerticalRVAdapter(
            activity!!.applicationContext,
            arrayListVertical, this
        )
    }
    private val viewModel: DeezerViewModel by activityViewModels()
    private val arrayListVertical by lazy { mutableListOf<VerticalModel>() }
    private val playlist by lazy { ArrayList<ITrack>() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity!!.findViewById<FrameLayout>(R.id.mini_player_container).visibility = View.GONE
        if (arrayListVertical.isEmpty()) {
            viewModel.apply {
                topAlbumLiveData.observe(viewLifecycleOwner, Observer {
                    arrayListVertical.add(
                        VerticalModel(
                            getString(R.string.chart_albums),
                            getString(R.string.chart_albums_description),
                            it
                        )
                    )
                    adapter.notifyDataSetChanged()
                })
                topTrackLiveData.observe(viewLifecycleOwner, Observer {
                    playlist.addAll(it.data)
                    arrayListVertical.add(
                        VerticalModel(
                            getString(R.string.chart_tracks),
                            getString(R.string.chart_tracks_description),
                            it
                        )
                    )
                    adapter.notifyDataSetChanged()
                })
            }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity!!.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onClickItem(response: ITrack) {
        if (activity!!.supportFragmentManager.findFragmentByTag(MiniPlayerFragment::class.java.toString()) == null) {
            activity!!.supportFragmentManager.apply {
                beginTransaction().replace(
                    R.id.mini_player_container,
                    MiniPlayerFragment(),
                    MiniPlayerFragment::class.java.toString()
                ).commit()
            }
            activity!!.supportFragmentManager.apply {
                beginTransaction().replace(
                    R.id.player_container,
                    PlayerFragment(),
                    PlayerFragment::class.java.toString()
                ).commit()
            }
        }
        serviceIntent.apply {
            action = ACTION_PREPARE
            putExtra(TRACK_INTENT, response)
            putParcelableArrayListExtra("playlist", playlist)
        }
        startForegroundService(activity!!.applicationContext, serviceIntent)
    }

    override fun onClickItem(response: IAlbum) {
        viewModel.getAlbumById(response.id)
        navController.navigate(R.id.albumPlayerFragment, Bundle().apply {
            putSerializable(TRANSFER_KEY, response)
        })
    }
}
