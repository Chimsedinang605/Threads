package com.example.threads.screen

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
import androidx.navigation.compose.*
import com.example.threads.R
import com.example.threads.model.BottomNavitem
import com.example.threads.navigation.Routes
import com.example.threads.screen.Login_Logout.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNav(navController: NavHostController){


    val navController1 = rememberNavController()

    Scaffold (bottomBar = { MyBottomBar(navController1) }) {
        innerPadding -> NavHost(navController = navController1, startDestination = Routes.Home.routes ,
        modifier  = Modifier.padding(innerPadding)) {

        composable(route = Routes.Home.routes) {
            Home(navController)
        }
        composable(Routes.Notification.routes){
            Notification()
        }
        composable(Routes.Profile.routes){
            Profile(navController)
        }
        composable(Routes.Search.routes){
            Search(navController)
        }
        composable(Routes.AddThread.routes){
            AddThreads(navController1)
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
    }
    }

}

@Composable
fun MyBottomBar(navController1: NavHostController) {

    val backStackEntry = navController1.currentBackStackEntryAsState()

    val list = listOf(

        BottomNavitem(
            "Home", Routes.Home.routes, ImageVector.vectorResource(id = R.drawable.home )
        ),

        BottomNavitem(
            "Search", Routes.Search.routes, ImageVector.vectorResource(id = R.drawable.search)
        ),

        BottomNavitem(
            "AddThreads", Routes.AddThread.routes, ImageVector.vectorResource(id = R.drawable.add_post)
        ),

        BottomNavitem(
            "Notification", Routes.Notification.routes, ImageVector.vectorResource(id = R.drawable.heart_icon)
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
            list.forEach { item ->
                val selected = item.route == backStackEntry.value?.destination?.route //backStackEntry?.value?.destination?.route

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable{
                            navController1.navigate(item.route) {
                                popUpTo(navController1.graph.findStartDestination().id) {
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
                            Color.Black  // Màu đen đậm khi selected
                        else Color.Black.copy(alpha = 0.4f)  // Màu đen nhạt hơn khi không được chọn
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
