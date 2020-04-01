package com.example.hitshub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.hitshub.R
import com.example.hitshub.listener.OnItemListener
import com.example.hitshub.models.VerticalModel
import com.example.hitshub.utils.Constants.EMPTY_STRING

class VerticalRVAdapter(
    private val context: Context,
    private val arrayList: List<VerticalModel>,
    private val onItemListener: OnItemListener
) :
    Adapter<ViewHolder>() {
    private val list = mutableListOf<HorizontalRVAdapter>()
    private lateinit var horizontalRVAdapter: HorizontalRVAdapter

    inner class VerticalRVViewHolder(itemView: View) : ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.horizontal_recycler_view)
        val title: TextView = itemView.findViewById(R.id.mini_player_title)
        val shortDescription: TextView = itemView.findViewById(R.id.description_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalRVViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_vertical, parent, false)
        return VerticalRVViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val verticalModel = arrayList[position]
        horizontalRVAdapter = HorizontalRVAdapter(verticalModel.item, onItemListener)
        list.add(horizontalRVAdapter)
        (holder as VerticalRVViewHolder).apply {
            if (verticalModel.description == EMPTY_STRING) {
                shortDescription.height = 0
            }
            title.text = verticalModel.title
            shortDescription.text = verticalModel.description
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = horizontalRVAdapter
        }
    }
}
