package com.example.threads.item_view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.threads.model.*
import com.example.threads.model.CommentModel
import com.example.threads.viewmodel.*
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CommentItem(
    comment: CommentModel,
    currentUserId: String,
) {
    val userViewModel: UserViewModel = viewModel()
    var user by remember { mutableStateOf<UserModel?>(null) }
    var expanded by remember { mutableStateOf(false) }

    // Lấy thông tin người dùng từ Firestore
    LaunchedEffect(comment.userId) {
        val userDoc = FirebaseFirestore.getInstance()
            .collection("users")
            .document(comment.userId)
            .get()

        userDoc.addOnSuccessListener { snapshot ->
            val fetchedUser = snapshot.toObject(UserModel::class.java)
            user = fetchedUser
        }.addOnFailureListener {
            // Xử lý lỗi ở đây (nếu cần)
        }
    }


    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Ảnh đại diện người dùng
            AsyncImage(
                model = user?.imgUrl ?: "R.drawable.default_profile",
                contentDescription = "Ảnh đại diện",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Tên người dùng và nội dung bình luận
                Text(
                    text = user?.username ?: "Người dùng",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = comment.comment,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Thời gian và các tùy chọn
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    // Chỉ hiển thị tùy chọn Trả lời
                    Text(
                        text = "Trả lời",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { /* Xử lý trả lời */ }
                    )

                    // Hiển thị nút xóa nếu bình luận thuộc về người dùng hiện tại
                    if (comment.userId == currentUserId) {
                        Spacer(modifier = Modifier.width(16.dp))

                        Box {
                            IconButton(
                                onClick = { expanded = true },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Tùy chọn",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}

// Hàm định dạng thời gian
fun formatTimestamp(date: Date): String {
    val now = Calendar.getInstance()
    val commentTime = Calendar.getInstance()
    commentTime.time = date

    val diffInMillis = now.timeInMillis - commentTime.timeInMillis
    val diffInMinutes = diffInMillis / (60 * 1000)
    val diffInHours = diffInMillis / (60 * 60 * 1000)
    val diffInDays = diffInMillis / (24 * 60 * 60 * 1000)

    return when {
        diffInMinutes < 1 -> "Vừa xong"
        diffInMinutes < 60 -> "${diffInMinutes}p"
        diffInHours < 24 -> "${diffInHours}h"
        diffInDays < 7 -> "${diffInDays}d"
        else -> {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.format(date)
        }
    }
}