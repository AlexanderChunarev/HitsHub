package com.example.hitshub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hitshub.R
import com.example.hitshub.activities.BaseActivity.Companion.USER
import com.example.hitshub.adapter.MessageRecyclerViewAdapter
import com.example.hitshub.media.Player
import com.example.hitshub.models.Message
import com.example.hitshub.models.User
import com.google.firebase.database.FirebaseDatabase

class ChatFragment : Fragment() {
    private val database by lazy {
        FirebaseDatabase.getInstance().getReference("messages")
    }
    private val messages by lazy { mutableListOf<Message>() }
    private val player by lazy { Player.getInstance() }
    private val user by lazy { activity!!.intent.getSerializableExtra(USER) as User }
    private val adapter by lazy {
        MessageRecyclerViewAdapter(messages)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Picasso.get().load(player.track.artist!!.pictureBig)
//            .transform(BlurTransformation(activity!!.applicationContext, 25, 2))
//            .into(background_imageView)
//        chat_recycler_view.apply {
//            smoothScrollToPosition(messages.size)
//            layoutManager =
//                LinearLayoutManager(
//                    activity!!.applicationContext,
//                    LinearLayoutManager.VERTICAL,
//                    false
//                )
//            adapter = this@ChatFragment.adapter
//        }
//        send_message_button.setOnClickListener {
//            if (message_editText.text.isNotEmpty()) {
//                Message(
//                    name = user.name,
//                    avatarUrl = user.avatarUrl,
//                    content = message_editText.text.toString(),
//                    trackId = player.track.id,
//                    time = TimeUnit.MILLISECONDS.toSeconds(player.currentPosition.toLong()).toInt()
//                ).apply {
//                    database.push().setValue(this)
//                }
//            }
//        }
//        database.orderByChild("trackId")
//            .equalTo(player.track.id.toDouble()).addChildEventListener(object : ChildEventListener {
//                override fun onCancelled(databaseError: DatabaseError) {
//                }
//
//                override fun onChildMoved(dataSnapshot: DataSnapshot, p1: String?) {
//                }
//
//                override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
//                }
//
//                override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
//                    dataSnapshot.getValue(Message::class.java).apply {
//                        messages.add(this!!)
//                        adapter.notifyItemInserted(messages.size)
//                    }
//                }
//
//                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
//                }
//            })
    }

    companion object {
        const val ORDER_BY_TRACK = "order_by_track"
    }
}
