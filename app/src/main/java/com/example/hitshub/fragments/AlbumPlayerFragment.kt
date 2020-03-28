package com.example.hitshub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.hitshub.R
import com.example.hitshub.adapter.AlbumRecyclerViewAdapter
import com.example.hitshub.fragments.PlayerFragment.Companion.TRANSFER_KEY
import com.example.hitshub.listener.OnItemListener
import com.example.hitshub.media.Player
import com.example.hitshub.models.IAlbum
import com.example.hitshub.models.ITrack
import com.example.hitshub.models.Track
import com.example.hitshub.viewmodels.DeezerViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_album_player.*

class AlbumPlayerFragment : BaseMediaFragment(), OnItemListener {
    override val adapter by lazy {
        AlbumRecyclerViewAdapter(
            playlist,
            this
        )
    }
    private val viewModel: DeezerViewModel by activityViewModels()
    private lateinit var album: IAlbum
    private val playlist by lazy { mutableListOf<ITrack>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            album = arguments!!.getSerializable(TRANSFER_KEY) as IAlbum
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
        play_button.setOnClickListener {
            playlist[0].run {
                serviceIntent.putExtra(Player.TRACK_INTENT, this)
                startForegroundService(activity!!.applicationContext, serviceIntent)
                navController.navigate(R.id.player_fragment, Bundle().apply {
                    putSerializable(TRANSFER_KEY, this@run)
                })
            }
        }
        Picasso.get().load(album.artist.pictureBig).into(imageView)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getAlbumById.observe(viewLifecycleOwner, Observer {
            player.playlist.clear()
            it.albumTracks.data.toMutableList().forEach { albumTrack ->
                Track(
                    albumTrack.id,
                    albumTrack.title,
                    albumTrack.preview,
                    album.artist
                ).apply {
                    playlist.add(this)
                    player.playlist.add(this)
                }
            }
            adapter.notifyDataSetChanged()
            description_textView.text =
                StringBuilder().append("${album.title}, ${playlist.size} tracks")
        })
    }

    override fun onClickItem(response: ITrack) {
        serviceIntent.putExtra(Player.TRACK_INTENT, response)
        startForegroundService(activity!!.applicationContext, serviceIntent)
        navController.navigate(R.id.player_fragment, Bundle().apply {
            putSerializable(TRANSFER_KEY, response)
        })
    }

    override fun onClickItem(response: IAlbum) {}

    companion object {
        const val ARTIST_KEY = "artist_key"
    }
}
