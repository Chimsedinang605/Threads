package com.example.threads.item_view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
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
import com.example.threads.viewmodel.HomeViewModel

@Composable
fun ThreadItem(
    thread: ThreadModel,
    users: UserModel,
    navController: NavHostController,
    userId: String,
    viewModel: HomeViewModel = viewModel() // Get instance of the view model

) {
    val timeAgo = viewModel.formatTimeAgo(thread.timeStam) // Assuming timestamp is stored in thread model

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
                contentDescription = "User avatar",
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
            Icon(
                painter = painterResource(id = R.drawable.favorite),
                contentDescription = "Like",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                painter = painterResource(id = R.drawable.chat),
                contentDescription = "Comment",
                tint = Color.Black,
                modifier = Modifier.size(22.dp)
            )
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

            // Time indicator
            Text(
                text = timeAgo, // This would come from thread model in a real implementation
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Divider between posts
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(
            color = Color(0xFFEEEEEE),
            thickness = 0.5.dp
        )
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun ShowThreadList(){
////    ThreadItem()
//}