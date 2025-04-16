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