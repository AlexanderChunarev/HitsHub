package com.example.hitshub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.hitshub.R
import com.example.hitshub.adapter.VerticalRVAdapter
import com.example.hitshub.fragments.PlayerFragment.Companion.TRANSFER_KEY
import com.example.hitshub.models.IAlbum
import com.example.hitshub.models.ITrack
import com.example.hitshub.models.VerticalModel
import com.example.hitshub.viewmodels.DeezerViewModel
import java.util.*

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
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            activity?.let {
                val chatView = it.findViewById<ConstraintLayout>(R.id.chat_fragment_view)
                val playerView = it.findViewById<FrameLayout>(R.id.player_container)
                when {
                    (chatView != null && chatView.isVisible) -> {
                        (it.findViewById(R.id.fagment_player_lay) as MotionLayout).transitionToStart()
                    }
                    playerView.isVisible -> {
                        (it.findViewById(R.id.motion_base) as MotionLayout).transitionToStart()
                    }
                    else -> {
                        it.finish()
                    }
                }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
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

        requireActivity().onBackPressedDispatcher.addCallback(callback)
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
