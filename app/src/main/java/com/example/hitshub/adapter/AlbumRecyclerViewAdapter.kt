package com.example.hitshub.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.hitshub.R
import com.example.hitshub.listener.OnItemListener
import com.example.hitshub.models.ITrack

class AlbumRecyclerViewAdapter(
    private val playlist: MutableList<ITrack>,
    private val onItemListener: OnItemListener
) :
    Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return PlaylistViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.album_track_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return playlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as PlaylistViewHolder).bind(playlist, onItemListener)
    }
}
