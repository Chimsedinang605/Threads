package com.example.threads.viewmodel

import android.util.*
import androidx.lifecycle.*
import com.example.threads.model.*
import com.google.firebase.database.*

class HomeViewModel: ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    val thread = db.reference.child("threads")

    private val _threadAndUsers = MutableLiveData<List< Pair <ThreadModel, UserModel>>>()
    val threadAndUsers: LiveData<List<Pair<ThreadModel, UserModel>>> = _threadAndUsers

    private val _error = MutableLiveData<String>()


}