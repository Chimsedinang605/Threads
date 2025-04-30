package com.example.threads.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.threads.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class SearchViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val usersRef = db.collection("users")

    private val _userList = MutableLiveData<List<UserModel>>()
    val userList: LiveData<List<UserModel>> = _userList

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        usersRef.get()
            .addOnSuccessListener { snapshot ->
                val result = mutableListOf<UserModel>()
                for (document in snapshot.documents) {
                    val user = document.toObject<UserModel>()
                    user?.let { result.add(it) }
                }
                _userList.value = result
            }
            .addOnFailureListener { e ->
                Log.e("SearchViewModel", "Error fetching users: ${e.message}")
                _userList.value = emptyList()
            }
    }

    fun fetchUserFromThread(thread: ThreadModel, onResult: (UserModel) -> Unit) {
        usersRef.document(thread.userId)
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject<UserModel>()
                user?.let(onResult)
            }
            .addOnFailureListener { e ->
                Log.e("SearchViewModel", "Error fetching user: ${e.message}")
            }
    }
}
