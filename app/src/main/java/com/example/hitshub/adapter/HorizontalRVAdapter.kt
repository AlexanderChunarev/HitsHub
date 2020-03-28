package com.example.hitshub.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.hitshub.R
import com.example.hitshub.listener.OnItemListener
import com.example.hitshub.models.*

class HorizontalRVAdapter(
    val item: IRecyclerHorizontalModel,
    private val onItemListener: OnItemListener
) :
    Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: BaseViewHolder? = null
        when (viewType) {
            TYPE_TRACKS -> {
                view = TrackViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_horizontal,
                        parent,
                        false
                    )
                )
            }
            TYPE_ALBUMS -> {
                view = AlbumViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_horizontal,
                        parent,
                        false
                    )
                )
            }
        }
        return view!!
    }

    override fun getItemCount(): Int {
        return item.data.size
    }

    override fun getItemViewType(position: Int): Int {
        if (item is ChartTracksData || item is TrackData) {
            return TYPE_TRACKS
        } else if (item is ChartAlbumData || item is SearchAlbumData) {
            return TYPE_ALBUMS
        }
        return -1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_TRACKS -> {
                (holder as TrackViewHolder).bind(item, onItemListener)
            }
            TYPE_ALBUMS -> {
                (holder as AlbumViewHolder).bind(item, onItemListener)
            }
        }
    }

    companion object {
        const val TYPE_TRACKS = 1
        const val TYPE_ALBUMS = 2
    }
}
