package com.example.threads.viewmodel

import android.net.Uri
import android.util.*
import androidx.lifecycle.*
import com.cloudinary.android.*
import com.cloudinary.android.callback.*
import com.example.threads.model.*
import com.google.firebase.auth.*
import com.google.firebase.database.*

class AddThreadViewModel: ViewModel() {

    val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    val userRef = db.reference.child("threads")

    private val _isPost = MutableLiveData<Boolean>()
    val isPost: LiveData<Boolean> = _isPost

    private val _error = MutableLiveData<String>()


     fun saveData(
        thread: String,
        userID: String,
        imageUrl: String,
    ) {
        val threadData = ThreadModel( thread, imageUrl, userID, System.currentTimeMillis().toString() )

        userRef.child(userRef.push().key!!).setValue(threadData)
            .addOnSuccessListener {
                _isPost.postValue(true)
            }
            .addOnFailureListener {
                _isPost.postValue(false)
            }
    }
    // lưu ảnh trên cloudinary
     fun saveImage(
        thread: String,
        userID: String,
        imageUri: Uri
    ) {
        imageUri?.let { uri ->
            MediaManager.get().upload(uri)
                .unsigned("thread") // Thay thế bằng upload preset của bạn
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String) {
                        // Upload started
                        Log.d("CloudinaryUpload", "Upload started with requestId: $requestId")

                    }

                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                        // Upload progress
                        val progress = (bytes * 100 / totalBytes).toInt()
                        Log.d("CloudinaryUpload", "Upload progress: $progress%")
                    }

                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        val imageUrl = resultData["url"].toString()
                        saveData(thread, userID,imageUrl)
                    }

                    override fun onError(requestId: String, error: ErrorInfo) {
                        // Upload error
                        _error.postValue("Lỗi tải ảnh lên Cloudinary: ${error.description}")
                    }

                    override fun onReschedule(requestId: String, error: ErrorInfo) {
                        // Upload reschedule
                        Log.w("CloudinaryUpload", "Upload reschedule: ${error.description}")
                    }
                })
                .dispatch()
        }
    }
}
