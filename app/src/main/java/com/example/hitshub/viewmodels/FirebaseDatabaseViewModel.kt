package com.example.hitshub.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hitshub.models.Message
import com.example.hitshub.repositories.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirebaseDatabaseViewModel : ViewModel() {
    private val firebaseRepository by lazy { FirebaseRepository() }
    val messages by lazy { MutableLiveData<MutableList<Message>>() }

    fun push(message: Message) = GlobalScope.launch {
        firebaseRepository.pushMessage(message)
    }

    fun getMessages(id: String) = GlobalScope.launch {
        val response = firebaseRepository.fetchMessages(id)
        withContext(Dispatchers.Main) {
            messages.value = response
        }
    }
}
