package com.example.hitshub.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hitshub.R
import com.example.hitshub.listener.OnItemListener

abstract class BaseViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {
    var textViewTitle: TextView = itemView.findViewById(R.id.txtTitleHorizontal)
    var imageViewThumb: ImageView = itemView.findViewById(R.id.ivThumb)

    abstract fun bind(response: Any, clickListener: OnItemListener)
}
