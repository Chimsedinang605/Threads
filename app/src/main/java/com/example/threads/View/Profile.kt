package com.example.threads.View

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
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
import com.example.threads.Data.SharePref
import com.example.threads.item_view.ThreadItem
import com.example.threads.model.UserModel
import com.example.threads.navigation.Routes
import com.example.threads.viewmodel.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun Profile(navHostController: NavHostController) {
    val context = LocalContext.current
    // View Models
    val authViewModel: AuthViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()

    val firebaseUserState = authViewModel.firebaseUser.observeAsState(null)
    val firebaseUser = firebaseUserState.value
    var logoutTriggered by remember { mutableStateOf(false) }

    // Get current user ID from Firebase Auth
    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    val threads by userViewModel.threads.observeAsState(emptyList())
    val followerList by userViewModel.followerList.observeAsState(emptyList())
    val followingList by userViewModel.followingList.observeAsState(emptyList())

    // Fetch data when composable first loads
    LaunchedEffect(currentUserUid) {
        if (currentUserUid.isNotEmpty()) {
            userViewModel.fetchThreads(currentUserUid)
            userViewModel.getFollowers(currentUserUid)
            userViewModel.getFollowing(currentUserUid)

            userViewModel.fetchComments(currentUserUid)
        }
    }

    val user = UserModel(
        name = SharePref.getName(context),
        username = SharePref.getUsername(context),
        imgUrl = SharePref.getImageUrl(context),
    )

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
            delay(200)
            navHostController.navigate(Routes.LoginScreen.routes) {
                popUpTo(navHostController.graph.startDestinationId)
                    launchSingleTop = true
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        LazyColumn {
            // header
            item {
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
                        text = SharePref.getUsername(context),
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
                            painter = rememberAsyncImagePainter(model = SharePref.getImageUrl(context)),
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
                            ProfileStat(count = threads.size, label = "bài viết")
                            ProfileStat(count = followerList.size , label = "người theo dõi")
                            ProfileStat(count = followingList.size , label = "đang theo dõi")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Display Name and Bio
                    Text(
                        text = SharePref.getName(context),
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

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { /* Edit Profile */ },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Chỉnh sửa")
                        }

                        OutlinedButton(
                            onClick = {
                                logoutTriggered = true
                                authViewModel.logout()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Đăng xuất")
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

            items(threads ?: emptyList() ) { pair ->
                ThreadItem(
                    thread = pair,
                    users = user,
                    navHostController = navHostController,
                    userId = SharePref.getUsername(context)
                )

            }


        }
    }
}

@Composable
fun ProfileStat(count: Int, label: String) {
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