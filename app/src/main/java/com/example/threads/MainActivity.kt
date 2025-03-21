package com.example.threads

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.threads.navigation.NavGraph
import com.example.threads.ui.theme.ThreadsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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