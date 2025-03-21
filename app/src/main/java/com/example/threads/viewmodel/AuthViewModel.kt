package com.example.threads.viewmodel

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threads.Utils.SharePref
import com.example.threads.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AuthViewModel: ViewModel() {
    val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    val userRef = db.reference.child("users")

    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    val firebaseUser: LiveData<FirebaseUser?> = _firebaseUser

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        _firebaseUser.value = auth.currentUser
    }

    private fun isEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun login(userIdentifier: String, password: String, ) {

        if (userIdentifier.contains("@")) {


            if (isEmail(userIdentifier)) {
                loginWithFirebaseAuth(userIdentifier, password)
            } else {
                loginWithUsername(userIdentifier, password)
            }

        }


//        auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener{
//
//                if (it.isSuccessful){
//                    _firebaseUser.postValue(auth.currentUser)
//                }else{
//                    _error.postValue("Đăng nhập thất bại: ${it.exception?.message}")
//                }
//
//            }

//
//        // Query the database by username
//        userRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(
//            object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        for (userSnapshot in dataSnapshot.children) {
//                            val userData = userSnapshot.getValue(UserModel::class.java)
//
//                            if (userData != null && userData.password == password) {
//                                // If user exists and password matches, sign in with Firebase Auth
//                                if (userData.email.isNotEmpty()) {
//                                    auth.signInWithEmailAndPassword(userData.email, password)
//                                        .addOnCompleteListener { task ->
//                                            if (task.isSuccessful) {
//                                                _firebaseUser.postValue(auth.currentUser)
//                                            } else {
//                                                _error.postValue("Đăng nhập Firebase thất bại: ${task.exception?.message}")
//                                            }
//                                        }
//                                } else {
//                                    _error.postValue("Email không hợp lệ")
//                                }
//                                return
//                            }
//                        }
//                        _error.postValue("Sai mật khẩu")
//                    } else {
//                        _error.postValue("Không tìm thấy người dùng")
//                    }
//                }
//
//                override fun onCancelled(dataError: DatabaseError) {
//                    _error.postValue("Lỗi kết nối cơ sở dữ liệu: ${dataError.message}")
//                }
//            })


    }



    private fun loginWithUsername(username: String, password: String) {
        userRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                            for (userSnapshot in dataSnapshot.children) {
                                val userData = userSnapshot.getValue(UserModel::class.java)

                                if (userData != null) {
                                    if (userData.password == password) {
                                        // Kiểm tra email hợp lệ trước khi dùng Firebase Auth
                                        if (userData.email.isNotEmpty() && isEmail(userData.email)) {
                                            auth.signInWithEmailAndPassword(userData.email, password)
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        _firebaseUser.postValue(auth.currentUser)
                                                    } else {
                                                        _error.postValue("Đăng nhập Firebase thất bại: ${task.exception?.message}")
                                                    }
                                                }
                                        } else {
                                            _error.postValue("Email không hợp lệ")
                                        }
                                        return
                                    }
                                }
                            }
                            _error.postValue("Sai mật khẩu")
                    } else {

                        _error.postValue("Không tìm thấy người dùng")
                    }
                }

                override fun onCancelled(dataError: DatabaseError) {
                    _error.postValue("Lỗi kết nối cơ sở dữ liệu: ${dataError.message}")
                }
            })
    }


private fun loginWithFirebaseAuth(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                task ->
                if (task.isSuccessful) {
                    _firebaseUser.postValue(auth.currentUser)
                } else {
                    _error.postValue("Đăng nhập thất bại: ${task.exception?.message}")
                }
            }
    }

    fun register(
        name: String,
        username: String,
        email: String,
        password: String,
        context: Context
    ) {
        // First check if username already exists
        userRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        _error.postValue("Tên người dùng đã tồn tại")
                    } else {
                        proceedWithRegistration(name, username, email, password, context)
                    }
                }

                override fun onCancelled(dataError: DatabaseError) {
                    _error.postValue("Lỗi kiểm tra tên người dùng: ${dataError.message}")
                }
            })
    }

    private fun proceedWithRegistration(
        name: String,
        username: String,
        email: String,
        password: String,
        context: Context
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _firebaseUser.postValue(auth.currentUser)
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        saveData(name, username, email, password, "", uid, context)
                    } else {
                        _error.postValue("UID null, không thể lưu dữ liệu")
                    }
                } else {
                    _error.postValue("Tạo tài khoản thất bại: ${task.exception?.message}")
                }
            }
    }

    private fun saveData(
        name: String,
        username: String,
        email: String,
        password: String,
        imageUrl: String,
        uid: String,
        context: Context
    ) {
        val userData = UserModel(name, username, email, password, imageUrl, uid)

        userRef.child(uid).setValue(userData)
            .addOnSuccessListener {
                SharePref.storeData(name, username, email, imageUrl, context)
            }
            .addOnFailureListener { exception ->
                _error.postValue("Lỗi lưu dữ liệu vào Realtime Database: ${exception.message}")
                Log.e("AuthViewModel", "Lỗi lưu dữ liệu: ${exception.message}")
                Toast.makeText(context, "Lỗi lưu dữ liệu: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun logout() {
        auth.signOut()
        _firebaseUser.postValue(null)
    }
}