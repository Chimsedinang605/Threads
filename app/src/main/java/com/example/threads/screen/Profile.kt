package com.example.threads.screen

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.threads.navigation.Routes
import com.example.threads.viewmodel.AuthViewModel

@Composable
fun Profile(navController : NavController){

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUserState = authViewModel.firebaseUser.observeAsState(null)
    val firebaseUser = firebaseUserState.value


    LaunchedEffect(firebaseUser) {

        if( firebaseUser == null) {
            navController.navigate(Routes.LoginScreen.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    Text(text = "Profile", modifier = Modifier.clickable{
        authViewModel.logout()
    })

}