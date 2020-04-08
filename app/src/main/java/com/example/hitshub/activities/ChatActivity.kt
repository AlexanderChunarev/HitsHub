package com.example.hitshub.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hitshub.R
import com.example.hitshub.adapter.MessageRecyclerViewAdapter
import com.example.hitshub.models.Message
import com.example.hitshub.models.User
import com.example.hitshub.viewmodels.FirebaseDatabaseViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    private val adapter by lazy { MessageRecyclerViewAdapter() }
    private val firebaseViewModel: FirebaseDatabaseViewModel by viewModels()
    private lateinit var user: User
    private var trackID: Long? = null
    private var messageAt: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        with(chat_recycler_view) {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = this@ChatActivity.adapter
            smoothScrollToPosition(this@ChatActivity.adapter.itemCount)
        }

        intent.apply {
            user = getParcelableExtra(USER_KEY)!!
            trackID = getLongExtra(TRACK_ID_KEY, 0)
            messageAt = getIntExtra(WRITE_MESSAGE_AT, 0)
        }

        send_message_button.setOnClickListener {
            if (message_editText.text.isNotEmpty()) {
                Message(
                    name = user.name,
                    avatarUrl = user.avatarUrl,
                    content = message_editText.text.toString(),
                    trackId = trackID!!,
                    time = messageAt!!.toInt()
                ).apply {
                    firebaseViewModel.push(this)
                }
            }
            message_editText.text = null
        }

        Firebase.firestore
            .collection("messages")
            .whereEqualTo("trackId", trackID)
            .addSnapshotListener { querySnapshot, _ ->
                querySnapshot?.documentChanges.let {
                    it!!.forEach { change ->
                        adapter.addItem(change.document.toObject(Message::class.java))
                        chat_recycler_view.scrollToPosition(adapter.itemCount - 1)
                    }
                }
            }
    }

    companion object {
        const val TRACK_ID_KEY = "track_id_key"
        const val WRITE_MESSAGE_AT = "write_message_at"
        const val USER_KEY = "user_chat_key"
    }
}
