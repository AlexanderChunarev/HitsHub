package com.example.hitshub.fragments

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.hitshub.R
import com.example.hitshub.adapter.VerticalRVAdapter
import com.example.hitshub.models.IAlbum
import com.example.hitshub.models.ITrack
import com.example.hitshub.models.VerticalModel
import com.example.hitshub.utils.Constants.EMPTY_STRING
import com.example.hitshub.viewmodels.DeezerViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*

class SearchFragment : BaseFragment() {
    override val adapter by lazy {
        VerticalRVAdapter(
            activity!!.applicationContext,
            arrayListOf(), this
        )
    }
    private val viewModel: DeezerViewModel by activityViewModels()
    private val playlist by lazy { ArrayList<ITrack>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parent_recycler_view.adapter = adapter
        val toolbar = toolbar as Toolbar
        toolbar.title = "Search"
        (activity!! as AppCompatActivity).setSupportActionBar(toolbar as Toolbar?)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.apply {
            getTrackByName.observe(viewLifecycleOwner, Observer {
                playlist.addAll(it.data)
                adapter.addItem(
                    VerticalModel(
                        getString(R.string.searched_tracks),
                        EMPTY_STRING,
                        it
                    )
                )
            })
            getAlbumByName.observe(viewLifecycleOwner, Observer {
                adapter.addItem(
                    VerticalModel(
                        getString(R.string.searched_albums),
                        EMPTY_STRING,
                        it
                    )
                )
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_fragment_menu, menu)
        val searchView = menu.findItem(R.id.search_view_btn).actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.clear()
                adapter.notifyDataSetChanged()
                viewModel.apply {
                    getTrackByName(name = query!!)
                    getAlbumByName(name = query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
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
