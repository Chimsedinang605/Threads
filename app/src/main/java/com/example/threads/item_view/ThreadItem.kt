package com.example.threads.item_view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threads.R
import com.example.threads.model.*
import com.example.threads.navigation.Routes
import com.example.threads.viewmodel.HomeViewModel
import com.example.threads.viewmodel.UserViewModel

@Composable
fun ThreadItem(
    thread: ThreadModel,
    users: UserModel,
    navHostController: NavHostController,
    currentUserUid: String,
    userViewModel: UserViewModel = viewModel()
) {
    val viewModel: HomeViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Header: User info (avatar, name)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User avatar
            Image(
                painter = rememberAsyncImagePainter(model = users.imgUrl),
                contentDescription = "Ảnh đại diện",
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            // sau avt la ten nguoi dung (name)
            Text(
                text = users.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // More options icon
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options",
                tint = Color.DarkGray,
                modifier = Modifier.size(20.dp)
            )
        }

        // Post content - Image (if exists)
        if (thread.image.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = thread.image),
                    contentDescription = "Post image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Action buttons row (like, comment, share)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LikeButton(
                postId = thread.threadId,
                currentUserUid = currentUserUid
            )

            // Comment button
            IconButton(
                onClick = {
                    val routes = Routes.CommentsScreen.routes.replace("{postId}", thread.threadId)
                    navHostController.navigate(routes)

                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.chat),
                    contentDescription = "Comment",
                    tint = Color.Black,
                    modifier = Modifier.size(22.dp)
                )
            }

        }

        // Post content - Text
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            if (thread.thread.isNotEmpty()) {
                Row(verticalAlignment = Alignment.Top) {
                    // nick name
                    Text(
                        text = users.username,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = " ${thread.thread}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                }
            }
        }

        // Divider between posts
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(
            color = Color(0xFFEEEEEE),
            thickness = 0.5.dp
        )
    }
}
@Composable
fun LikeButton(
    postId: String,
    currentUserUid: String,
    modifier: Modifier = Modifier
) {
    val userViewModel: UserViewModel = viewModel()
    var isLiked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableStateOf(0L) }

    // Effect để lấy trạng thái like ban đầu
    LaunchedEffect(postId, currentUserUid) {
        userViewModel.getLikeStatus(postId, currentUserUid) { count, liked ->
            likeCount = count
            isLiked = liked
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        IconButton(
            onClick = {
                if (isLiked) {
                    userViewModel.unLike(postId, currentUserUid)
                } else {
                    userViewModel.likePost(postId, currentUserUid)
                }
                // UI sẽ được cập nhật tự động thông qua listener
            }
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (isLiked) "Unlike" else "Like",
                tint = if (isLiked) Color.Red else Color.Gray
            )
        }

        Text(
            text = "$likeCount",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}