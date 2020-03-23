package com.example.hitshub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hitshub.R
import com.example.hitshub.adapter.VerticalRVAdapter
import com.example.hitshub.listener.OnItemListener
import com.example.hitshub.models.IAlbum
import com.example.hitshub.models.ITrack
import com.example.hitshub.models.VerticalModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(), OnItemListener {
    private val adapter by lazy {
        VerticalRVAdapter(
            activity!!.applicationContext,
            arrayListVertical, this
        )
    }
    private val arrayListVertical by lazy { mutableListOf<VerticalModel>() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parent_recycler_view.apply {
            layoutManager =
                LinearLayoutManager(
                    activity!!.applicationContext,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = this@HomeFragment.adapter
        }
        setData()
    }

    private fun setData() {
        viewModel.getTopAlbums()
        viewModel.topAlbumLiveData.observe(viewLifecycleOwner, Observer {
            arrayListVertical.add(VerticalModel("Chart albums", it))
            adapter.notifyDataSetChanged()
        })


        viewModel.getTopTracks()
        viewModel.topTrackLiveData.observe(viewLifecycleOwner, Observer {
            arrayListVertical.add(VerticalModel("Chart tracks", it))
            adapter.notifyDataSetChanged()
        })
    }

    override fun onClickItem(response: ITrack) {
        Toast.makeText(
            activity,
            response.toString(),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onClickItem(response: IAlbum) {
        Toast.makeText(
            activity,
            response.toString(),
            Toast.LENGTH_LONG
        ).show()
    }
}
