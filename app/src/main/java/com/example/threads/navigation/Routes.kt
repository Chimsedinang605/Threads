package com.example.threads.navigation

sealed class Routes(val routes: String) {

    object Home : Routes("home")
    object Profile : Routes("profile")
    object Search : Routes("search")
    object Splash : Routes("splash")
    object BottomNav : Routes("bottom_nav")
    object AddThread : Routes("add_thread")
    object LoginScreen : Routes("login_screen")
    object RegisterScreen : Routes("register_screen")
    object ForgetScreen : Routes("forget_screen")
    object ResetPass : Routes("reset_screen")
    object StartedScreen : Routes("start_screen")
    object OtherUser : Routes("other_user/{userId}")
    object CommentsScreen : Routes("comment_screen/{postId}")
    object EditProfile : Routes("edit_profile")


}