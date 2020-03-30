package com.example.hitshub.repositories

import com.example.hitshub.models.Message
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseRepository {

    suspend fun pushMessage(message: Message) = withContext(Dispatchers.IO) {
        Firebase.firestore
            .collection("messages")
            .document().set(message, SetOptions.merge())
            .await()
    }

    suspend fun fetchMessages(id: String): MutableList<Message> = withContext(Dispatchers.IO) {
        Firebase.firestore
            .collection("messages")
            .whereEqualTo("trackId", id.toLong())
            .get().await()
            .toObjects(Message::class.java)
    }

    private suspend fun <T> Task<T>.await(): T {
        if (isComplete) {
            val e = exception
            return if (e == null) {
                if (isCanceled) {
                    throw CancellationException("Task $this was cancelled normally.")
                } else {
                    @Suppress("UNCHECKED_CAST")
                    result as T
                }
            } else {
                throw e
            }
        }
        return suspendCancellableCoroutine { cont ->
            addOnCompleteListener {
                val e = exception
                if (e == null) {
                    @Suppress("UNCHECKED_CAST")
                    if (isCanceled) cont.cancel() else cont.resume(result as T)
                } else {
                    cont.resumeWithException(e)
                }
            }
        }
    }
}
