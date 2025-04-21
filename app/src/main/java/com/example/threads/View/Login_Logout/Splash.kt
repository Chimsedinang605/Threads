package com.example.threads.View.Login_Logout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.threads.*
import com.example.threads.navigation.Routes
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun Splash(navController: NavHostController){

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {

        val (image) = createRefs()

        Image(painter = painterResource(id = R.drawable.threadss), contentDescription = "logo",
            modifier = Modifier.constrainAs(image) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

}

//    Text(text = "Splash")
    LaunchedEffect(true) {
        delay(1000)

        if (FirebaseAuth.getInstance().currentUser != null)
        navController.navigate(Routes.BottomNav.routes){
            popUpTo(navController.graph.startDestinationId)
            launchSingleTop = true
        }
        else
            navController.navigate(Routes.StartedScreen.routes){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
    }
}