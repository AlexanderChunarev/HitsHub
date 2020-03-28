package com.example.hitshub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.hitshub.R
import com.example.hitshub.adapter.VerticalRVAdapter
import com.example.hitshub.fragments.PlayerFragment.Companion.TRANSFER_KEY
import com.example.hitshub.media.Player
import com.example.hitshub.models.IAlbum
import com.example.hitshub.models.ITrack
import com.example.hitshub.models.VerticalModel
import com.example.hitshub.viewmodels.DeezerViewModel

class HomeFragment : BaseMediaFragment() {
    override val adapter by lazy {
        VerticalRVAdapter(
            activity!!.applicationContext,
            arrayListVertical, this
        )
    }
    private val viewModel: DeezerViewModel by activityViewModels()
    private val arrayListVertical by lazy { mutableListOf<VerticalModel>() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
                    player.playlist = it.data.toMutableList()
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
    }

    override fun onClickItem(response: ITrack) {
        serviceIntent.putExtra(Player.TRACK_INTENT, response)
        startForegroundService(activity!!.applicationContext, serviceIntent)
        navController.navigate(R.id.player_fragment, Bundle().apply {
            putSerializable(TRANSFER_KEY, response)
        })
    }

    override fun onClickItem(response: IAlbum) {
        viewModel.getAlbumById(response.id)
        navController.navigate(R.id.albumPlayerFragment, Bundle().apply {
            putSerializable(TRANSFER_KEY, response)
        })
    }
}
