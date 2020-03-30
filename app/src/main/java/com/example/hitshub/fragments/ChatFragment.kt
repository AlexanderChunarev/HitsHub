package com.example.hitshub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hitshub.R
import com.example.hitshub.activities.BaseActivity.Companion.USER
import com.example.hitshub.adapter.MessageRecyclerViewAdapter
import com.example.hitshub.media.Player
import com.example.hitshub.models.Message
import com.example.hitshub.models.User
import com.example.hitshub.viewmodels.FirebaseDatabaseViewModel
import kotlinx.android.synthetic.main.fragment_chat.*
import java.util.concurrent.TimeUnit

class ChatFragment : Fragment() {
    private val messages by lazy { mutableListOf<Message>() }
    private val player by lazy { Player.getInstance() }
    private val firebaseViewModel by lazy { FirebaseDatabaseViewModel() }
    private val user by lazy { activity!!.intent.getSerializableExtra(USER) as User }
    private val adapter by lazy {
        MessageRecyclerViewAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        firebaseViewModel.apply {
            // getMessages(player.track.id.toString())
            messages.observe(viewLifecycleOwner, Observer {
                it.forEach { message ->
                    adapter.messages.add(message)
                    adapter.notifyDataSetChanged()
                }
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Picasso.get().load(player.track.artist!!.pictureBig)
//            .transform(BlurTransformation(activity!!.applicationContext, 25, 2))
//            .into(background_imageView)
        chat_recycler_view.apply {
            smoothScrollToPosition(messages.size)
            if (this@ChatFragment.adapter.messages.isNotEmpty()) {
                smoothScrollToPosition(this@ChatFragment.adapter.messages.size)
            }
            layoutManager =
                LinearLayoutManager(
                    activity!!.applicationContext,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = this@ChatFragment.adapter
        }
        send_message_button.setOnClickListener {
            if (message_editText.text.isNotEmpty()) {
                Message(
                    name = user.name,
                    avatarUrl = user.avatarUrl,
                    content = message_editText.text.toString(),
                    trackId = 0,
                    time = TimeUnit.MILLISECONDS.toSeconds(player.currentPosition.toLong()).toInt()
                ).apply {
                    firebaseViewModel.push(this)
                }
            }
        }
    }

    companion object {
        const val ORDER_BY_TRACK = "order_by_track"
    }
}
