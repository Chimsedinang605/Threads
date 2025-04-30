package com.example.threads.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.cloudinary.android.*
import com.cloudinary.android.callback.*
import com.example.threads.model.*
import com.google.firebase.Firebase
import com.google.firebase.auth.*
import com.google.firebase.firestore.firestore

class AddThreadViewModel: ViewModel() {

    val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val threadsCollection = db.collection("threads")
    private val usersCollection = db.collection("users")

    private val _isPost = MutableLiveData<Boolean>()
    val isPost: LiveData<Boolean> = _isPost

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun saveData(
        thread: String,
        imageUrl: String,
        userID: String
    ) {
        usersCollection.document(userID).get()
            .addOnSuccessListener { document ->
                val name = document.getString("name") ?: "Người dùng ẩn danh"

                // Tạo ID mới cho thread
                val threadId = threadsCollection.document().id

                val threadData = ThreadModel(
                    threadId = threadId,
                    thread = thread,
                    image = imageUrl,
                    userId = userID,
                    name = name,
                    timeStam = System.currentTimeMillis().toString()
                )

                threadsCollection.document(threadId).set(threadData)
                    .addOnSuccessListener {
                        _isPost.postValue(true)
                    }
                    .addOnFailureListener { e ->
                        _isPost.postValue(false)
                        _error.postValue("Lỗi khi lưu thread: ${e.message}")
                    }
            }
            .addOnFailureListener { error ->
                Log.e("AddThreadViewModel", "Lỗi khi tìm nạp tên người dùng: $error")

                // Tạo ID mới cho thread
                val threadId = threadsCollection.document().id

                val threadData = ThreadModel(
                    threadId = threadId,
                    thread = thread,
                    image = imageUrl,
                    userId = userID,
                    name = "Người dùng ẩn danh", // Mặc định khi lỗi
                    timeStam = System.currentTimeMillis().toString()
                )

                threadsCollection.document(threadId).set(threadData)
                    .addOnSuccessListener {
                        _isPost.postValue(true)
                    }
                    .addOnFailureListener { e ->
                        _isPost.postValue(false)
                        _error.postValue("Lỗi khi lưu thread: ${e.message}")
                    }
            }
    }

    // lưu ảnh trên cloudinary
    fun saveImage(
        thread: String,
        imageUri: Uri,
        userID: String,
    ) {
        imageUri.let { uri ->
            MediaManager.get().upload(uri)
                .unsigned("thread") // Thay thế bằng upload preset của bạn
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String) {
                        // Upload started
                    }

                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                        // Upload progress
                    }

                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        val imageUrl = resultData["url"].toString()
                        saveData(thread, imageUrl, userID)
                    }

                    override fun onError(requestId: String, error: ErrorInfo) {
                        // Upload error
                        _error.postValue("Lỗi tải ảnh lên Cloudinary: ${error.description}")
                    }

                    override fun onReschedule(requestId: String, error: ErrorInfo) {
                        // Upload reschedule
                    }
                })
                .dispatch()
        }
    }
}