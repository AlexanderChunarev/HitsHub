package com.example.hitshub.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.hitshub.R
import com.example.hitshub.models.Message

class MessageRecyclerViewAdapter() :
    Adapter<ViewHolder>() {
    val messages by lazy { mutableListOf<Message>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ChatMessageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.chat_message_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ChatMessageViewHolder).bind(messages)
    }
}
