package com.example.threads.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threads.model.*
import com.google.firebase.Firebase
import com.google.firebase.database.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

class UserViewModel: ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    val threadRef = db.getReference("threads")
    val userRef = db.getReference("users")

    private val _threads = MutableLiveData(listOf<ThreadModel>())
    val threads: LiveData<List<ThreadModel>> get() = _threads

    private val _followerList = MutableLiveData(listOf<String>())
    val followerList: LiveData<List<String>> get() = _followerList

    private val _followingList = MutableLiveData(listOf<String>())
    val followingList: LiveData<List<String>> get() = _followingList

    private val _users = MutableLiveData(UserModel())
    val users: LiveData<UserModel> get() = _users

    fun fetchUser(uid: String) {
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                user?.let {
                    _users.postValue(it)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun fetchThreads(uid: String) {
        threadRef.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val threadList = snapshot.children.mapNotNull {
                    it.getValue(ThreadModel::class.java)
                }
                _threads.postValue(threadList)
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    val firestoreDb = Firebase.firestore

    fun followUser(userId: String, currentUserUid: String) {

        val followingref = firestoreDb.collection("following").document(currentUserUid)
        val followerRef = firestoreDb.collection("followers").document(userId)

        followingref.update("followingIds", FieldValue.arrayUnion(userId))
        followerRef.update("followerIds", FieldValue.arrayUnion(currentUserUid))
    }

    fun getFollowers(userId: String) {
        firestoreDb.collection("followers").document(userId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                if (value != null && value.exists()) {
                    val followerIds = value.get("followerIds") as? List<String> ?: listOf()
                    _followerList.postValue(followerIds)
                } else {
                    // Document doesn't exist, set empty list
                    _followerList.postValue(emptyList())
                }
            }
    }

    fun getFollowing(userId: String) {
        firestoreDb.collection("following").document(userId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                if (value != null && value.exists()) {
                    val followingIds = value.get("followingIds") as? List<String> ?: listOf()
                    _followingList.postValue(followingIds)
                } else {
                    // Document doesn't exist, set empty list
                    _followingList.postValue(emptyList())
                }
            }
    }
}