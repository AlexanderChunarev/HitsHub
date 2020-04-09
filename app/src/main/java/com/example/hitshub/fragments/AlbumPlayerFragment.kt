package com.example.hitshub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.hitshub.R
import com.example.hitshub.adapter.AlbumRecyclerViewAdapter
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
        setBackPressCallback()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_album_player, container, false)
    }

    private fun setBackPressCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (activity!!.findViewById<LinearLayout>(R.id.player_container).isVisible) {
                    motionLayout.transitionToStart()
                } else {
                    navController.navigate(R.id.navigation_home)
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parent_recycler_view.adapter = adapter
        play_button.setOnClickListener {
            adapter.playlist[0].run {
                callMediaPlayer(this, adapter.playlist)
            }
        }
        Picasso.get().load(album.cover_url).into(imageView)
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
