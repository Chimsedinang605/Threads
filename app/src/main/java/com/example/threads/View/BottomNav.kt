package com.example.threads.View

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.threads.R
import com.example.threads.View.Login_Logout.*
import com.example.threads.model.BottomNavitem
import com.example.threads.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNav(navController: NavHostController) {
    // Create a nested NavController for the bottom navigation
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = { MyBottomBar(bottomNavController) }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = Routes.Home.routes,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Routes.Home.routes) {
                Home(navController)
            }

            composable(Routes.Profile.routes){
                Profile(bottomNavController)
            }
            composable(Routes.Search.routes){
                Search(bottomNavController)
            }
            composable(Routes.AddThread.routes){
                AddThreads(bottomNavController)
            }

            composable(Routes.LoginScreen.routes){
                LoginScreen(navController)
            }
            composable(Routes.RegisterScreen.routes){
                RegisterScreen(navController)
            }
            composable(Routes.ForgetScreen.routes){
                ForgetScreen(navController)
            }
            composable(Routes.ResetPass.routes){
                ResetPass(navController)
            }
            composable(Routes.StartedScreen.routes){
                StartedScreen(navController)
            }
            composable(Routes.EditProfile.routes){
                EditProfile(navController)
            }

            composable(
                route = Routes.OtherUser.routes, // "other_user/{userId}"
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId")
                OtherUser(userId = userId!!, navHostController =  navController)
            }

            composable(
                route = Routes.CommentsScreen.routes,
                arguments = listOf(navArgument("postId") { type = NavType.StringType })
            ) { backStackEntry ->
                val postId = backStackEntry.arguments?.getString("postId")
                if (postId != null) {
                    CommentsScreen(postId = postId, navHostController = navController)
                }
            }


        }
    }
}

// Improved MyBottomBar with consistent navigation
@Composable
fun MyBottomBar(navController: NavHostController) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route

    val navItems = listOf(
        BottomNavitem(
            "Home", Routes.Home.routes, ImageVector.vectorResource(id = R.drawable.home)
        ),
        BottomNavitem(
            "Search", Routes.Search.routes, ImageVector.vectorResource(id = R.drawable.search)
        ),
        BottomNavitem(
            "AddThreads",
            Routes.AddThread.routes,
            ImageVector.vectorResource(id = R.drawable.add_post)
        ),
        BottomNavitem(
            "Notification",
            Routes.Notification.routes,
            ImageVector.vectorResource(id = R.drawable.heart_icon)
        ),
        BottomNavitem(
            "Profile", Routes.Profile.routes, ImageVector.vectorResource(id = R.drawable.person)
        )
    )

    BottomAppBar(
        modifier = Modifier.height(54.dp),
        containerColor = Color.White,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        tonalElevation = 8.dp,
        windowInsets = BottomAppBarDefaults.windowInsets
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                val selected = item.route == currentRoute

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        .padding(4.dp)
                        .width(65.dp)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        modifier = Modifier
                            .size(25.dp)
                            .scale(if (selected) 1.2f else 1f),
                        tint = if (selected)
                            Color.Black
                        else Color.Black.copy(alpha = 0.4f)
                    )

                    if (selected) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .width(26.dp)
                                .height(2.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(1.dp)
                                )
                        )
                    }
                }
            }
        }
    }
}