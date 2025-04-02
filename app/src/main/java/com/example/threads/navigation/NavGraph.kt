package com.example.threads.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.threads.screen.*

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(navController = navController,
        startDestination = Routes.Splash.routes ) {

        composable(Routes.Splash.routes){
            Splash(navController)
        }
        composable(Routes.Home.routes){
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
            ForgetScreen( navController)
        }
        composable(Routes.ResetPass.routes){
            ResetPass( navController)
        }

    }
}
