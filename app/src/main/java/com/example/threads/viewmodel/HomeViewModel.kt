package com.example.threads.viewmodel

import android.util.*
import androidx.lifecycle.*
import com.example.threads.model.*
import com.google.firebase.database.*

class HomeViewModel: ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    val thread = db.getReference("threads")

    private val _threadAndUsers = MutableLiveData<List< Pair <ThreadModel, UserModel>>>()
    val threadAndUsers: LiveData<List<Pair<ThreadModel, UserModel>>> = _threadAndUsers

    init {
        fetchThreadsAndUsers {
            _threadAndUsers.value  = it
        }
    }

    // Add this utility function somewhere accessible in your project
    fun formatTimeAgo(timestamp: String): String {
        try {
            val postTime = timestamp.toLong()
            val currentTime = System.currentTimeMillis()
            val difference = currentTime - postTime

            // Convert to appropriate time unit
            return when {
                difference < 60_000 -> "just now"
                difference < 3_600_000 -> "${difference / 60_000}m ago" // minutes
                difference < 86_400_000 -> "${difference / 3_600_000}h ago" // hours
                difference < 604_800_000 -> "${difference / 86_400_000}d ago" // days
                difference < 2_592_000_000 -> "${difference / 604_800_000}w ago" // weeks
                difference < 31_536_000_000 -> "${difference / 2_592_000_000}mo ago" // months
                else -> "${difference / 31_536_000_000}y ago" // years
            }
        } catch (e: Exception) {
            return "Unknown time"
        }
    }

    private fun fetchThreadsAndUsers( onResult: (List<Pair<ThreadModel, UserModel>>) -> Unit) {
        thread.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val result = mutableListOf<Pair<ThreadModel, UserModel>>()
                for (threadSnapshot in snapshot.children) {
                    val thread = threadSnapshot.getValue(ThreadModel::class.java)
                    thread.let {
                        fetchUserFromThread(it!!) {
                            user ->
                            result.add(0,Pair(it, user))

                            if (result.size == snapshot.childrenCount.toInt()) {
                                onResult(result)
                            }
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun fetchUserFromThread(thread: ThreadModel, onResult:(UserModel) -> Unit) {
        db.getReference("users").child(thread.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    user?.let(onResult)
//                    if (user != null) {
//                        onResult(user)
//                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }


}