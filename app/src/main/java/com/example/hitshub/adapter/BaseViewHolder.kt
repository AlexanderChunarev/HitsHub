package com.example.hitshub.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.hitshub.listener.OnItemListener

abstract class BaseViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    abstract fun bind(response: Any, clickListener: OnItemListener)
}
