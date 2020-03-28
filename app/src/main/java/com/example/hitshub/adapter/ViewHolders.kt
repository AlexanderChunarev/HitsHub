package com.example.hitshub.adapter

import android.view.View
import com.example.hitshub.listener.OnItemListener
import com.example.hitshub.models.*
import com.squareup.picasso.Picasso

class TrackViewHolder(itemView: View) :
    BaseViewHolder(itemView) {

    override fun bind(response: Any, clickListener: OnItemListener) {
        var horizontalModel: Track? = null
        when (response) {
            is ChartTracksData -> horizontalModel =
                response.data[adapterPosition]
            is TrackData -> horizontalModel =
                response.data[adapterPosition]
        }

        horizontalModel!!.apply {
            textViewTitle.text = title
            Picasso.get().load(this.artist.picture).into(imageViewThumb)
        }
        itemView.setOnClickListener {
            clickListener.onClickItem(horizontalModel)
        }
    }
}

class AlbumViewHolder(itemView: View) :
    BaseViewHolder(itemView) {

    override fun bind(response: Any, clickListener: OnItemListener) {
        var horizontalModel: Album? = null
        when (response) {
            is ChartAlbumData -> horizontalModel =
                response.data[adapterPosition]
            is SearchAlbumData -> horizontalModel =
                response.data[adapterPosition]
        }
        horizontalModel!!.apply {
            textViewTitle.text = title
            Picasso.get().load(cover_url).into(imageViewThumb)
        }
        itemView.setOnClickListener {
            clickListener.onClickItem(horizontalModel)
        }
    }
}
