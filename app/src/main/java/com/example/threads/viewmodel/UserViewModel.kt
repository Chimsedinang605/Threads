package com.example.threads.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.threads.model.*
import com.google.firebase.Firebase
import com.google.firebase.database.*
import com.google.firebase.firestore.*

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

    private val _CommentList = MutableLiveData(listOf<CommentModel>())
    val CommentList: LiveData<List<CommentModel>> get() = _CommentList

    private val _LikeList = MutableLiveData(listOf<String>())
    val LikeList: LiveData<List<String>> get() = _LikeList

    // ListenerRegistration để ngừng lắng nghe bình luận khi ViewModel bị hủy
    private var commentsListenerRegistration: ListenerRegistration? = null


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
        threadRef.orderByChild("userId").equalTo(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
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

    val comments = mutableStateOf<List<CommentModel>>(listOf())
    val commentRef = db.getReference("comments")
    /**
     * Thêm bình luận mới vào một thread cụ thể
     */
    fun addComment(
        threadId: String,
        userId: String,
        comment: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Kiểm tra các tham số đầu vào
        if (threadId.isEmpty()) {
            onFailure(IllegalArgumentException("ThreadId không được để trống"))
            return
        }

        if (userId.isEmpty()) {
            onFailure(IllegalArgumentException("UserId không được để trống"))
            return
        }

        if (comment.isBlank()) {
            onFailure(IllegalArgumentException("Nội dung bình luận không được để trống"))
            return
        }

        // Tạo dữ liệu bình luận
        val commentData = hashMapOf(
            "comment" to comment,
            "threadId" to threadId,
            "userId" to userId
//            "timestamp" to FieldValue.serverTimestamp()
        )

        // Thêm bình luận vào Firestore
        firestoreDb.collection("comments")
            .document(threadId)
            .collection("thread_comments")
            .add(commentData)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    /**
     * Lấy danh sách bình luận của một thread
     */
    fun fetchComments(threadId: String) {
        // Hủy lắng nghe cũ nếu có
        commentsListenerRegistration?.remove()

        if (threadId.isEmpty()) {
            _CommentList.postValue(emptyList())
            return
        }

        // Đăng ký lắng nghe mới
        commentsListenerRegistration = firestoreDb.collection("comments")
            .document(threadId)
            .collection("thread_comments")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _CommentList.postValue(emptyList())
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val commentList = snapshot.documents.mapNotNull { doc ->
                        try {
                            val data = doc.data ?: return@mapNotNull null
                            CommentModel(
                                userId = data["userId"] as? String ?: "",
                                comment = data["comment"] as? String ?: ""
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                    _CommentList.postValue(commentList)
                } else {
                    _CommentList.postValue(emptyList())
                }
            }
    }
}