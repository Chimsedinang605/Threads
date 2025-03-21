package com.example.threads.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threads.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage
import java.util.UUID

class AuthViewModel: ViewModel() {
    val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("users")

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")

    private val _firebaseUser = MutableLiveData<FirebaseUser>()
    val firebaseUser: LiveData<FirebaseUser> = _firebaseUser

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        _firebaseUser.value = auth.currentUser
    }

    fun login(
        email: String,
        password: String
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _firebaseUser.postValue(auth.currentUser)
                } else {
                    _error.postValue("Something went wrong")
                }
            }
    }

    fun register(
        name: String,
        username: String,
        email: String,
        password: String,
        imageUri: Uri?,
        context: Context
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _firebaseUser.postValue(auth.currentUser)
                    saveImage(name, username, email, password, imageUri, auth.currentUser?.uid, context)
                } else {
                    _error.postValue("Something went wrong")
                }
            }
    }

    private fun saveImage(
        name: String,
        username: String,
        email: String,
        password: String,
        imageUri: Uri?,
        uid: String?,
        context: Context
    ) {
        val imageUploadTask = imageRef.putFile(imageUri!!)
        imageUploadTask.addOnCompleteListener {
            if (it.isSuccessful) {
                imageRef.downloadUrl.addOnCompleteListener{
                    saveData( name, username, email, password, it.result.toString(), uid, context )


                }
            }
        }
    }

    private fun saveData(
        name: String,
        username: String,
        email: String,
        password: String,
        toString: String,
        uid: String?,
        context: Context
    ) {
        val userData = UserModel(name, username, email, password, toString)
        userRef.child(uid!!).setValue(userData)
            .addOnSuccessListener{

            }
            .addOnFailureListener{
                _error.postValue("Something went wrong")
            }

    }



}