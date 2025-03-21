package com.example.threads.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.threads.model.BottomNavitem
import com.example.threads.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNav(navController: NavHostController){


    val navController1 = rememberNavController()

    Scaffold (bottomBar = { MyBottomBar(navController1) }) {
        innerPadding -> NavHost(navController = navController1, startDestination = Routes.Home.routes ,
        modifier  = Modifier.padding(innerPadding)) {

        composable(route = Routes.Home.routes) {
            Home()
        }
        composable(Routes.Notification.routes){
            Notification()
        }
        composable(Routes.Profile.routes){
            Profile(navController)
        }
        composable(Routes.Search.routes){
            Search()
        }
        composable(Routes.AddThread.routes){
            AddThreads()
        }

        composable(Routes.LoginScreen.routes){
            LoginScreen(navController)
        }
        composable(Routes.RegisterScreen.routes){
            RegisterScresn(navController)
        }
        composable(Routes.ForgetScreen.routes){
            ForgetScreen(navController)
        }
        composable(Routes.ResetPass.routes){
            ResetPass(navController)
        }
    }
    }

}

@Composable
fun MyBottomBar(navController1: NavHostController) {

    val backStackEntry = navController1.currentBackStackEntryAsState()

    val list = listOf(

        BottomNavitem(
            "Home", Routes.Home.routes, Icons.Rounded.Home
        ),

        BottomNavitem(
            "Search", Routes.Search.routes, Icons.Rounded.Search
        ),

        BottomNavitem(
            "AddThreads", Routes.AddThread.routes, Icons.Rounded.Add
        ),

        BottomNavitem(
            "Notification", Routes.Notification.routes, Icons.Rounded.Notifications
        ),

        BottomNavitem(
            "Profile", Routes.Profile.routes, Icons.Rounded.Person
        )


    )

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        tonalElevation = 8.dp,
        windowInsets = BottomAppBarDefaults.windowInsets
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            list.forEach { item ->
                val selected = item.route == backStackEntry?.value?.destination?.route

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clickable {
                            navController1.navigate(item.route) {
                                popUpTo(navController1.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        modifier = Modifier
                            .size(32.dp)
                            .scale(if (selected) 1.2f else 1f),
                        tint = if (selected)
                            Color.Black  // Màu đen đậm khi selected
                        else Color.Black.copy(alpha = 0.4f)  // Màu đen nhạt hơn khi không được chọn
                    )

                    if (selected) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(16.dp)
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