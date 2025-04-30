package com.example.threads.View

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.threads.R
import com.example.threads.item_view.CommentItem
import com.example.threads.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsScreen(
    postId: String,
    navHostController: NavHostController
) {
    val userViewModel: UserViewModel = viewModel()
    val commentsList by userViewModel.CommentList.observeAsState(emptyList())
    var commentInput by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Get current user ID
    var currentUserUid = ""
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser != null) {
        currentUserUid = currentUser.uid
    }

    LaunchedEffect(key1 =  postId) {
        userViewModel.fetchComments(postId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bình luận") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.navigateUp() }) {
                        Icon(painter = painterResource(id = R.drawable.back), contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = commentInput,
                        onValueChange = { commentInput = it },
                        placeholder = { Text("Viết bình luận...") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.LightGray,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {

                            if (commentInput.isNotBlank()) {
                                userViewModel.addComment(
                                    threadId = postId,
                                    userId = currentUserUid,
                                    comment = commentInput,
                                    onSuccess = {
                                        commentInput = "" // Xóa nội dung nhập sau khi bình luận thành công
                                        // Danh sách bình luận sẽ tự động cập nhật nhờ snapshot listener
                                    },
                                    onFailure = { e ->
                                        Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }

                        },
                        enabled = commentInput.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Gửi",
                            tint = if (commentInput.isNotBlank()) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        if (commentsList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Chưa có bình luận nào",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(commentsList) { comment ->
                    CommentItem(
                        comment = comment,
                        currentUserId = currentUserUid,

                    )
                }
            }
        }
    }
}
