package com.example.hitshub.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.hitshub.R
import com.example.hitshub.extentions.format
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
            textViewArtist.text = artist!!.name
            Picasso.get().load(artist!!.picture).fit().into(imageViewThumb)
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
            textViewArtist.text = artist.name
            Picasso.get().load(cover_url).fit().into(imageViewThumb)
        }
        itemView.setOnClickListener {
            clickListener.onClickItem(horizontalModel)
        }
    }
}

class PlaylistViewHolder(itemView: View) :
    ViewHolder(itemView) {
    private val titleText: TextView = itemView.findViewById(R.id.title_text)
    private val artistText: TextView = itemView.findViewById(R.id.artist_textView)
    private val coverImage: ImageView = itemView.findViewById(R.id.cover_image)

    fun bind(playlist: MutableList<ITrack>, clickListener: OnItemListener) {
        playlist[adapterPosition].apply {
            titleText.text = title
            artistText.text = artist!!.name
            Picasso.get().load(artist!!.picture).into(coverImage)
            itemView.setOnClickListener {
                clickListener.onClickItem(this)
            }
        }
    }
}

class ChatMessageViewHolder(itemView: View) :
    ViewHolder(itemView) {
    private val message: TextView = itemView.findViewById(R.id.chat_message_textView)
    private val info: TextView = itemView.findViewById(R.id.author_message_textView)
    private val avatarImage: ImageView = itemView.findViewById(R.id.avatar_imageView)

    fun bind(messages: MutableList<Message>) {
        messages[adapterPosition].apply {
            info.text = StringBuilder().append("$name at ${time.format("mm:ss")}")
            message.text = content
            Picasso.get().load(avatarUrl).fit().into(avatarImage)
        }
    }
}
