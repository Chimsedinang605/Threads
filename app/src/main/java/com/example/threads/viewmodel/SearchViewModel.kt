package com.example.threads.viewmodel

import androidx.lifecycle.*
import com.example.threads.model.*
import com.google.firebase.database.*

class SearchViewModel: ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    private val usersRef = db.getReference("users")

    private val _userList = MutableLiveData<List<UserModel>>()
    val userList: LiveData<List<UserModel>> = _userList

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<UserModel>()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(UserModel::class.java)
                    user?.let { result.add(it) }
                }
                _userList.value = result
            }

            override fun onCancelled(error: DatabaseError) {
                // Consider logging error or showing a message
                _userList.value = emptyList()
            }
        })
    }

    fun fetchUserFromThread(thread: ThreadModel, onResult:(UserModel) -> Unit) {
        db.getReference("users").child(thread.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    user?.let(onResult)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Consider logging error
                }
            })
    }
}