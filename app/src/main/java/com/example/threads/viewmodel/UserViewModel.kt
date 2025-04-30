package com.example.threads.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.threads.model.*
import com.google.firebase.Firebase
import com.google.firebase.firestore.*

class UserViewModel: ViewModel() {

    private val db = Firebase.firestore
    val threadRef = db.collection("threads")
    val userRef = db.collection("users")

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
        userRef.document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(UserModel::class.java)
                    user?.let {
                        _users.postValue(it)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Handle error
                Log.e("ProfileViewModel", "Error fetching user: ${e.message}")
            }
    }
    fun fetchThreads(uid: String) {
        threadRef.whereEqualTo("userId", uid).get()
            .addOnSuccessListener { querySnapshot ->
                val threadList = querySnapshot.documents.mapNotNull { doc ->
                    doc.toObject(ThreadModel::class.java)
                }
                _threads.postValue(threadList)
            }
            .addOnFailureListener { e ->
                // Handle error
                Log.e("ProfileViewModel", "Error fetching threads: ${e.message}")
            }
    }

    val firestoreDb = Firebase.firestore
    /*
    * Follow
    * */
    fun followUser(userId: String, currentUserUid: String) {
        val followingRef = firestoreDb.collection("following").document(currentUserUid)
        val followerRef = firestoreDb.collection("followers").document(userId)

        // Sử dụng batch để đảm bảo tính nhất quán của dữ liệu
        val batch = firestoreDb.batch()

        batch.update(followingRef, "followingIds", FieldValue.arrayUnion(userId))
        batch.update(followerRef, "followerIds", FieldValue.arrayUnion(currentUserUid))
        batch.commit()

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
        /*
        * hủy follow
        * */
    fun unfollowUser(userId: String, currentUserUid: String) {
        val followingRef = firestoreDb.collection("following").document(currentUserUid)
        val followerRef = firestoreDb.collection("followers").document(userId)

        // Sử dụng batch để đảm bảo tính nhất quán của dữ liệu
        val batch = firestoreDb.batch()

        batch.update(followingRef, "followingIds", FieldValue.arrayRemove(userId))
        batch.update(followerRef, "followerIds", FieldValue.arrayRemove(currentUserUid))
        batch.commit()

    }

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

    fun likePost(postId: String, currentUserUid: String) {
        val postRef = firestoreDb.collection("threads").document(postId)
        val userLikeRef = firestoreDb.collection("userLikes").document(currentUserUid)

        // Sử dụng batch để đảm bảo tính nhất quán của dữ liệu
        val batch = firestoreDb.batch()

        batch.update(postRef, "likedBy", FieldValue.arrayUnion(currentUserUid))
        batch.update(postRef, "likeCount", FieldValue.increment(1))

        userLikeRef.get().addOnSuccessListener{ document ->
            if (document.exists() ) {
                batch.update(userLikeRef, "likedPosts", FieldValue.arrayUnion(postId))
            } else {
                batch.set(userLikeRef, hashMapOf("likedPosts" to arrayListOf(postId)))
            }
            batch.commit()
        }
    }
    fun unLike(postId: String, currentUserUid: String) {
        val postRef = firestoreDb.collection("threads").document(postId)
        val userLikeRef = firestoreDb.collection("userLikes").document(currentUserUid)

        val batch = firestoreDb.batch()

        // Gỡ userId khỏi mảng likedBy và giảm likeCount
        batch.update(postRef, "likedBy", FieldValue.arrayRemove(currentUserUid))
        batch.update(postRef, "likeCount", FieldValue.increment(-1))

        // Gỡ postId khỏi danh sách likedPosts của user
        userLikeRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                batch.update(userLikeRef, "likedPosts", FieldValue.arrayRemove(postId))
            }
            batch.commit()
        }
    }


    fun getLikeStatus(postId: String, currentUserUid: String, onUpdate: (likeCount: Long, isLiked: Boolean) -> Unit) {
        firestoreDb.collection("threads").document(postId)
            .addSnapshotListener { postSnapshot, error ->
                if (error != null || postSnapshot == null) return@addSnapshotListener

                val likeCount = postSnapshot.getLong("likeCount") ?: 0
                val likedBy = postSnapshot.get("likedBy") as? List<String> ?: listOf()
                val isLiked = currentUserUid in likedBy

                onUpdate(likeCount, isLiked)
            }
    }

    fun updateUserProfile(
        user: UserModel,
        bio: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (user.uid.isEmpty()) {
            onFailure(IllegalArgumentException("User ID cannot be empty"))
            return
        }

        val userDocument = userRef.document(user.uid)

        // Create a map of fields to update
        val updates = hashMapOf<String, Any>(
            "name" to user.name,
            "username" to user.username,
            "bio" to bio
        )

        // Only update image URL if it's not empty
        if (user.imgUrl.isNotEmpty()) {
            updates["imgUrl"] = user.imgUrl
        }

        // Update document in Firestore
        userDocument.update(updates)
            .addOnSuccessListener {
                Log.d("UserViewModel", "User profile updated successfully")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("UserViewModel", "Error updating user profile", exception)

                // If document doesn't exist yet, create it
                if (exception is FirebaseFirestoreException &&
                    exception.code == FirebaseFirestoreException.Code.NOT_FOUND) {

                    // Create full user document
                    val userData = hashMapOf(
                        "uid" to user.uid,
                        "name" to user.name,
                        "username" to user.username,
                        "email" to user.email,
                        "bio" to bio
                    )

                    if (user.imgUrl.isNotEmpty()) {
                        userData["imgUrl"] = user.imgUrl
                    }

                    userDocument.set(userData)
                        .addOnSuccessListener {
                            Log.d("UserViewModel", "User profile created successfully")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Log.e("UserViewModel", "Error creating user profile", e)
                            onFailure(e)
                        }
                } else {
                    onFailure(exception)
                }
            }
    }

}