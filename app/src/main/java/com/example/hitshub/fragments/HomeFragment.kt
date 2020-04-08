package com.example.hitshub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.hitshub.R
import com.example.hitshub.adapter.VerticalRVAdapter
import com.example.hitshub.models.IAlbum
import com.example.hitshub.models.ITrack
import com.example.hitshub.models.VerticalModel
import com.example.hitshub.viewmodels.DeezerViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class HomeFragment : BaseFragment() {
    override val adapter by lazy {
        VerticalRVAdapter(
            activity!!.applicationContext,
            arrayListOf(), this
        )
    }
    private val viewModel: DeezerViewModel by activityViewModels()
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
        parent_recycler_view.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.apply {
            topAlbumLiveData.observe(viewLifecycleOwner, Observer {
                adapter.addItem(
                    VerticalModel(
                        getString(R.string.chart_albums),
                        getString(R.string.chart_albums_description),
                        it
                    )
                )
            })
            topTrackLiveData.observe(viewLifecycleOwner, Observer {
                playlist.addAll(it.data)
                adapter.addItem(
                    VerticalModel(
                        getString(R.string.chart_tracks),
                        getString(R.string.chart_tracks_description),
                        it
                    )
                )
            })
        }
    }

    override fun onClickItem(response: ITrack) {
        callMediaPlayer(response, playlist)
    }

    override fun onClickItem(response: IAlbum) {
        viewModel.getAlbumById(response.id)
        navController.navigate(R.id.albumPlayerFragment, Bundle().apply {
            putSerializable(TRANSFER_KEY, response)
        })
    }
}
