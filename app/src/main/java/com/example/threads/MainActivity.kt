package com.example.threads

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.cloudinary.android.MediaManager
import com.example.threads.navigation.NavGraph
import com.example.threads.ui.theme.ThreadsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initconfig()
        setContent {
            ThreadsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {

                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }

    private fun initconfig() {
// ... inside your Application class or onCreate of an Activity
        MediaManager.init(this, mapOf(
            "cloud_name" to "dd8ng49fc", // Replace with your cloud name
            "api_key" to "your472332937763654_api_key", // Replace with your api key
            "api_secret" to "E-vQHvPhdxK3Ag3123SRyCVu_yk" // Replace with your api secret
        ))
    }
}

//class MainActivity : ComponentActivity() {
//    private val authViewModel: AuthViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
//            ThreadsTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    val navController = rememberNavController()
//
//                    // Correctly observe LiveData in Compose
//                    val currentUser = authViewModel.firebaseUser.observeAsState().value
//
//                    // Observe authentication state
//                    LaunchedEffect(currentUser) {
//                        if (currentUser == null) {
//                            // User not logged in, navigate to login screen
//                            navController.navigate("login") {
//                                popUpTo(navController.graph.startDestinationId) {
//                                    inclusive = true
//                                }
//                            }
//                        } else {
//                            // User logged in, navigate to home screen
//                            navController.navigate("home") {
//                                popUpTo(navController.graph.startDestinationId) {
//                                    inclusive = true
//                                }
//                            }
//                        }
//                    }
//
//                    NavGraph(navController = navController)
//                }
//            }
//        }
//    }
//}