package com.example.hitshub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.hitshub.R
import com.example.hitshub.extentions.format
import com.example.hitshub.models.Message
import com.squareup.picasso.Picasso

class MessageRecyclerViewAdapter :
    Adapter<MessageRecyclerViewAdapter.ChatMessageViewHolder>() {
    val messages by lazy { mutableListOf<Message>() }

    fun clear() {
        messages.clear()
        notifyDataSetChanged()
    }

    fun addAll(mutableList: MutableList<Message>) {
        messages.addAll(mutableList)
        notifyDataSetChanged()
    }

    fun addItem(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
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

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    inner class ChatMessageViewHolder(itemView: View) :
        ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.chat_message_textView)
        private val info: TextView = itemView.findViewById(R.id.author_message_textView)
        private val avatarImage: ImageView = itemView.findViewById(R.id.avatar_imageView)

        fun bind(message: Message) {
            message.apply {
                info.text = StringBuilder().append("$name at ${time.format("mm:ss")}")
                messageText.text = this.content
                Picasso.get().load(avatarUrl).fit().into(avatarImage)
            }
        }
    }
}
