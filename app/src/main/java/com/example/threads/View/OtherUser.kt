package com.example.threads.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threads.item_view.ThreadItem
import com.example.threads.navigation.Routes
import com.example.threads.viewmodel.AuthViewModel
import com.example.threads.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay


@Composable
fun OtherUser(userId: String,navHostController: NavHostController) {
    val context = LocalContext.current

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUserState = authViewModel.firebaseUser.observeAsState(null)
    val firebaseUser = firebaseUserState.value
    var logoutTriggered by remember { mutableStateOf(false) }

    val userViewModel: UserViewModel = viewModel()
    val threads by userViewModel.threads.observeAsState(emptyList())
    val users by userViewModel.users.observeAsState(null)

    val followerList by userViewModel.followerList.observeAsState(emptyList())
    val followingList by userViewModel.followingList.observeAsState(emptyList())

    var isFollowing by remember { mutableStateOf(false) }

    var currentUserUid = ""
    if (FirebaseAuth.getInstance().currentUser != null) {
        currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
    }


    // Fetch data only once when the composable is created
    LaunchedEffect(userId) {
        userViewModel.fetchUser(userId)
        userViewModel.fetchThreads(userId)
        userViewModel.getFollowers(userId)
        userViewModel.getFollowing(userId)
    }

    LaunchedEffect(followerList) {
        isFollowing = followerList != null && followerList!!.isNotEmpty()
                && followerList!!.contains(currentUserUid)
    }

    // Check if user is logged in
    LaunchedEffect(firebaseUser) {
        if (firebaseUser == null && !logoutTriggered) {
            navHostController.navigate(Routes.LoginScreen.routes) {
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    LaunchedEffect(logoutTriggered) {
        if (logoutTriggered) {
            // Give the logout operation time to complete
            delay(200)
            // Navigate to login screen
            navHostController.navigate(Routes.LoginScreen.routes) {
                // Clear the back stack to prevent navigating back to the OtherUser  screen
                popUpTo(Routes.Home.routes)
                launchSingleTop = true
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        if (users == null) {
            // Show loading indicator
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn {
                item {
                    // Custom App Bar instead of TopAppBar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                                                // Username header
                        Text(
                            text = users!!.username,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }

                    // Profile Header
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // User Info Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Profile Image
                            Image(
                                painter = rememberAsyncImagePainter(model = users!!.imgUrl),
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, Color.LightGray, CircleShape),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(32.dp))

                            // Statistics
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                ProfileOther(count = threads.size, label = "bài viết")
                                ProfileOther(count = followerList.size , label = "người theo dõi")
                                ProfileOther(count = followingList.size , label = "đang theo dõi")
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Display Name and Bio
                        Text(
                            text = users!!.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(2.dp))
                        // bio
                        Text(
                            text = "Threads User",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Action Buttons follow
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val buttonText = if (  followerList != null && followerList!!.isNotEmpty()
                                && followerList!!.contains(currentUserUid)  ) "Đang theo dõi" else "Theo dõi"
                            val buttonColors = if (isFollowing) {
                                ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color.Gray
                                )
                            } else {
                                ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color.Black
                                )
                            }

                            OutlinedButton(
                                onClick = {
//
                                    if (currentUserUid != "") {
                                        userViewModel.followUser(
                                            userId, currentUserUid
                                        )
                                    }

                                },
                                modifier = Modifier.weight(1f),
                                colors = buttonColors
                            ) {
                                Text(buttonText)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Divider
                        HorizontalDivider()
                    }
                }

                // Post Tabs (single tab for now)
                item {
                    TabRow(
                        selectedTabIndex = 0,
                        contentColor = Color.Black
                    ) {
                        Tab(
                            selected = true,
                            onClick = { },
                            icon = {
                                Icon(
                                    Icons.Outlined.GridOn,
                                    contentDescription = "Posts",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        )
                    }
                }

                // Threads/Posts
                items(threads ?: emptyList() ) { thread ->
                    ThreadItem(
                        thread = thread,
                        users = users!!,
                        navHostController = navHostController,
                        userId = currentUserUid
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileOther(count: Int, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            text = label,
            fontSize = 14.sp
        )
    }
}