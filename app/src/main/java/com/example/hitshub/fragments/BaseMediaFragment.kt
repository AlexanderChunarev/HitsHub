package com.example.hitshub.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hitshub.adapter.VerticalRVAdapter
import com.example.hitshub.listener.OnItemListener
import com.example.hitshub.models.VerticalModel
import com.example.hitshub.viewmodels.DeezerViewModel
import kotlinx.android.synthetic.main.fragment_home.*

abstract class BaseMediaFragment : Fragment(), OnItemListener {
    private val adapter by lazy {
        VerticalRVAdapter(
            activity!!.applicationContext,
            arrayListVertical, this
        )
    }
    private val viewModel by lazy {
        ViewModelProvider(activity!!).get(DeezerViewModel::class.java)
    }

    abstract val arrayListVertical: MutableList<VerticalModel>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parent_recycler_view.apply {
            layoutManager =
                LinearLayoutManager(
                    activity!!.applicationContext,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = this@BaseMediaFragment.adapter
        }

        if (arrayListVertical.isEmpty()) {
            viewModel.apply {
                topAlbumLiveData.observe(viewLifecycleOwner, Observer {
                    arrayListVertical.add(VerticalModel("Chart albums", it))
                    adapter.notifyDataSetChanged()
                })
                topTrackLiveData.observe(viewLifecycleOwner, Observer {
                    arrayListVertical.add(VerticalModel("Chart tracks", it))
                    adapter.notifyDataSetChanged()
                })
            }
        }
    }
}
