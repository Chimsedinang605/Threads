package com.example.threads.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.threads.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class HomeViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val threadsCollection = db.collection("threads")
    private val usersCollection = db.collection("users")

    private val _threadAndUsers = MutableLiveData<List<Pair<ThreadModel, UserModel>>>()
    val threadAndUsers: LiveData<List<Pair<ThreadModel, UserModel>>> = _threadAndUsers

    init {
        fetchThreadsAndUsers {
            _threadAndUsers.value = it
        }
    }

    fun formatTimeAgo(timestamp: String): String {
        return try {
            val postTime = timestamp.toLong()
            val currentTime = System.currentTimeMillis()
            val difference = currentTime - postTime

            when {
                difference < 60_000 -> "just now"
                difference < 3_600_000 -> "${difference / 60_000}m ago"
                difference < 86_400_000 -> "${difference / 3_600_000}h ago"
                difference < 604_800_000 -> "${difference / 86_400_000}d ago"
                difference < 2_592_000_000 -> "${difference / 604_800_000}w ago"
                difference < 31_536_000_000 -> "${difference / 2_592_000_000}mo ago"
                else -> "${difference / 31_536_000_000}y ago"
            }
        } catch (e: Exception) {
            "Unknown time"
        }
    }

    private fun fetchThreadsAndUsers(onResult: (List<Pair<ThreadModel, UserModel>>) -> Unit) {
        threadsCollection.get().addOnSuccessListener { snapshot ->
            val result = mutableListOf<Pair<ThreadModel, UserModel>>()
            val documents = snapshot.documents

            if (documents.isEmpty()) {
                onResult(result)
                return@addOnSuccessListener
            }

            for (doc in documents) {
                val thread = doc.toObject<ThreadModel>()
                thread?.let { threadModel ->
                    fetchUserFromThread(threadModel) { user ->
                        result.add(0, Pair(threadModel, user))

                        if (result.size == documents.size) {
                            onResult(result)
                        }
                    }
                }
            }
        }.addOnFailureListener {
            Log.e("HomeViewModel", "Error fetching threads: ${it.message}")
        }
    }

    fun fetchUserFromThread(thread: ThreadModel, onResult: (UserModel) -> Unit) {
        usersCollection.document(thread.userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.toObject<UserModel>()
                user?.let(onResult)
            }
            .addOnFailureListener {
                Log.e("HomeViewModel", "Error fetching user: ${it.message}")
            }
    }
}
