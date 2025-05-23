package com.example.threads.navigation

sealed class Routes(val routes: String) {

    object Home : Routes("home")
    object Notification : Routes("notification")
    object Profile : Routes("profile")
    object Search : Routes("search")
    object Splash : Routes("splash")
    object BottomNav : Routes("bottom_nav")
    object AddThread : Routes("add_thread")
    object LoginScreen : Routes("login_screen")
    object RegisterScreen : Routes("register_screen")
    object ForgetScreen : Routes("forget_screen")
    object ResetPass : Routes("reset_screen")


}