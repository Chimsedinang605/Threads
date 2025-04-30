package com.example.threads.navigation

import androidx.compose.runtime.*
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.threads.View.*
import com.example.threads.View.Login_Logout.*

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(navController = navController,
        startDestination = Routes.Splash.routes ) {

        composable(Routes.Splash.routes){
            Splash(navController)
        }
        composable(Routes.Home.routes){
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
            AddThreads(navController)
        }
        composable(Routes.BottomNav.routes){
            BottomNav(navController)
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
            route = Routes.CommentsScreen.routes,
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId")
            if (postId != null) {
                CommentsScreen(postId = postId, navHostController = navController)
            }
        }

        composable(
            route = Routes.OtherUser.routes, // "other_user/{userId}"
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            OtherUser(userId = userId!!, navHostController =  navController)
        }


    }
}