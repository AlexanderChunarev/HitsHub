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
import com.example.hitshub.adapter.AlbumRecyclerViewAdapter
import com.example.hitshub.fragments.PlayerFragment.Companion.TRANSFER_KEY
import com.example.hitshub.listener.OnItemListener
import com.example.hitshub.models.IAlbum
import com.example.hitshub.models.ITrack
import com.example.hitshub.models.Track
import com.example.hitshub.viewmodels.DeezerViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_album_player.*

class AlbumPlayerFragment : BaseFragment(), OnItemListener {
    override val adapter by lazy {
        AlbumRecyclerViewAdapter(this)
    }
    private val viewModel: DeezerViewModel by activityViewModels()
    private lateinit var album: IAlbum

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            album = arguments!!.getSerializable(TRANSFER_KEY) as IAlbum
        }
    }

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
                        navController.navigate(R.id.navigation_home)
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
        return inflater.inflate(R.layout.fragment_album_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        play_button.setOnClickListener {
            adapter.playlist[0].run {
                callMediaPlayer(this, adapter.playlist)
            }
        }
        Picasso.get().load(album.artist.pictureBig).fit().into(imageView)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getAlbumById.observe(viewLifecycleOwner, Observer {
            adapter.clear()
            it.albumTracks.data.toMutableList().forEach { albumTrack ->
                Track(
                    albumTrack.id,
                    albumTrack.title,
                    albumTrack.preview,
                    album.artist
                ).apply {
                    adapter.addItem(this)
                }
            }
            adapter.notifyDataSetChanged()
            description_textView.text =
                StringBuilder().append("${album.title}, ${adapter.playlist.size} tracks")
        })
    }

    override fun onClickItem(response: ITrack) {
        callMediaPlayer(response, adapter.playlist)
    }

    override fun onClickItem(response: IAlbum) {}
}
