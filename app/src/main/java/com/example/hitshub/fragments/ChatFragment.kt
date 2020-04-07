package com.example.hitshub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hitshub.R
import com.example.hitshub.activities.BaseActivity
import com.example.hitshub.adapter.MessageRecyclerViewAdapter
import com.example.hitshub.models.Message
import com.example.hitshub.models.User
import com.example.hitshub.viewmodels.FirebaseDatabaseViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_chat.*
import java.util.concurrent.TimeUnit

class ChatFragment : Fragment() {
    private val adapter by lazy {
        MessageRecyclerViewAdapter()
    }
    val navController by lazy { NavHostFragment.findNavController(this) }
    private val user by lazy { activity!!.intent.getSerializableExtra(BaseActivity.USER) as User }
    private val firebaseViewModel: FirebaseDatabaseViewModel by activityViewModels()
    private var trackID: Long? = null
    private var messageAt: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            trackID = it.getLong(TRACK_ID_KEY)
            messageAt = it.getLong(WRITE_MESSAGE_AT)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.navigate(R.id.player_fragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter.clear()
//        firebaseViewModel.apply {
//            getMessages(trackID!!)
//            messages.observe(viewLifecycleOwner, Observer {
//                it.forEach { message ->
//                    if (message.trackId == trackID) {
//                        adapter.addItem(message)
//                    }
//                }
//            })
//        }
        Firebase.firestore
            .collection("messages")
            .whereEqualTo("trackId", trackID)
            .addSnapshotListener { querySnapshot, _ ->
                querySnapshot?.documentChanges.let {
                    it!!.forEach { change ->
                        adapter.addItem(change.document.toObject(Message::class.java))
                    }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(chat_recycler_view) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity!!.applicationContext)
            adapter = this@ChatFragment.adapter
        }

        send_message_button.setOnClickListener {
            if (message_editText.text.isNotEmpty()) {
                Message(
                    name = user.name,
                    avatarUrl = user.avatarUrl,
                    content = message_editText.text.toString(),
                    trackId = trackID!!,
                    time = TimeUnit.MILLISECONDS.toSeconds(messageAt!!).toInt()
                ).apply {
                    firebaseViewModel.push(this)
                }
            }
            message_editText.text = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        firebaseViewModel.messages.removeObservers(viewLifecycleOwner)
    }

    companion object {
        const val TRACK_ID_KEY = "track_id_key"
        const val WRITE_MESSAGE_AT = "write_message_at"
        fun newInstance(trackId: Long, messageAt: Long) = ChatFragment().apply {
            arguments = Bundle().apply {
                putLong(TRACK_ID_KEY, trackId)
                putLong(WRITE_MESSAGE_AT, messageAt)
            }
        }
    }
}
