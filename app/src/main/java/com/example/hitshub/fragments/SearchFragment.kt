package com.example.hitshub.fragments

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.hitshub.R
import com.example.hitshub.adapter.VerticalRVAdapter
import com.example.hitshub.media.Player
import com.example.hitshub.models.IAlbum
import com.example.hitshub.models.ITrack
import com.example.hitshub.models.VerticalModel
import com.example.hitshub.viewmodels.DeezerViewModel

class SearchFragment : BaseFragment() {
    override val adapter by lazy {
        VerticalRVAdapter(
            activity!!.applicationContext,
            arrayListVertical, this
        )
    }
    private val viewModel: DeezerViewModel by activityViewModels()
    private val arrayListVertical by lazy { mutableListOf<VerticalModel>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.apply {
            getTrackByName.observe(viewLifecycleOwner, Observer {
                // player.playlist = it.data.toMutableList()
                arrayListVertical.add(VerticalModel(getString(R.string.searched_tracks), "", it))
                adapter.notifyDataSetChanged()
            })
            getAlbumByName.observe(viewLifecycleOwner, Observer {
                arrayListVertical.add(VerticalModel(getString(R.string.searched_albums), "", it))
                adapter.notifyDataSetChanged()
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_fragment_menu, menu)
        val searchView = menu.findItem(R.id.search_view_btn).actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                arrayListVertical.clear()
                adapter.notifyDataSetChanged()
                viewModel.getTrackByName(name = query!!)
                viewModel.getAlbumByName(name = query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPause() {
        super.onPause()
        arrayListVertical.clear()
        adapter.notifyDataSetChanged()
    }

    override fun onClickItem(response: ITrack) {
        serviceIntent.putExtra(Player.TRACK_INTENT, response)
        startForegroundService(activity!!.applicationContext, serviceIntent)
        navController.navigate(R.id.player_fragment, Bundle().apply {
            // putSerializable(PlayerFragment.TRANSFER_KEY, response)
        })
    }

    override fun onClickItem(response: IAlbum) {
        viewModel.getAlbumById(response.id)
        navController.navigate(R.id.albumPlayerFragment, Bundle().apply {
            putSerializable(PlayerFragment.TRANSFER_KEY, response)
        })
    }
}
