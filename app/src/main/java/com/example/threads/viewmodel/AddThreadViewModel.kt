package com.example.threads.viewmodel

import android.net.Uri
import android.util.Log
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
//        threadId: String,
        thread: String,
        imageUrl: String,
        userID: String
    ) {
         db.reference.child("users").child(userID).child(   "name")
             .get()
             .addOnSuccessListener{ snapshot ->
                 val name = snapshot.value.toString()
                 val newThreadRef = userRef.push()
                 val threadId = newThreadRef.key!! // Lấy ID tự động
                 val threadData = ThreadModel(
                     threadId,
                     thread,
                     imageUrl,
                     userID,
                     name = name,
                     System.currentTimeMillis().toString()
                 )

                 newThreadRef.setValue(threadData)
                     .addOnSuccessListener {
                         _isPost.postValue(true)
                     }
                     .addOnFailureListener {
                         _isPost.postValue(false)
                     }
             }
             .addOnFailureListener { error ->
                 Log.e("AddThreadViewModel", "Lỗi khi tìm nạp tên người dùng: $error")
                 val newThreadRef = userRef.push()
                 val threadId = newThreadRef.key!!
                 val threadData = ThreadModel(
                     threadId   = threadId,
                     thread = thread,
                     image = imageUrl,
                     userId = userID,
                     name = "Người dùng ẩn danh", // Mặc định khi lỗi
                     timeStam = System.currentTimeMillis().toString()
                 )
                 userRef.setValue(threadData)
                     .addOnSuccessListener {
                         _isPost.postValue(true)
                     }
                     .addOnFailureListener {
                         _isPost.postValue(false)
                     }
             }
         
     }

    // lưu ảnh trên cloudinary
     fun saveImage(
        thread: String,
        imageUri: Uri,
        userID: String,
    ) {
        imageUri?.let { uri ->
            MediaManager.get().upload(uri)
                .unsigned("thread") // Thay thế bằng upload preset của bạn
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String) {
                        // Upload started
//                        Log.d("CloudinaryUpload", "Upload started with requestId: $requestId")

                    }

                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                        // Upload progress
//                        val progress = (bytes * 100 / totalBytes).toInt()
//                        Log.d("CloudinaryUpload", "Upload progress: $progress%")
                    }

                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        val imageUrl = resultData["url"].toString()
                        saveData( thread, imageUrl,userID)
                    }

                    override fun onError(requestId: String, error: ErrorInfo) {
                        // Upload error
                        _error.postValue("Lỗi tải ảnh lên Cloudinary: ${error.description}")
                    }

                    override fun onReschedule(requestId: String, error: ErrorInfo) {
                        // Upload reschedule
//                        Log.w("CloudinaryUpload", "Upload reschedule: ${error.description}")
                    }
                })
                .dispatch()
        }
    }
}
