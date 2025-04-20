package com.example.threads.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.threads.item_view.ThreadItem
import com.example.threads.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Home(navHostController: NavHostController){
    val context= LocalContext.current
    val homeViewModel: HomeViewModel = viewModel()
    val threadAnduser by homeViewModel.threadAndUsers.observeAsState()

    LazyColumn {
        items(threadAnduser ?: emptyList()) { pairs ->
            ThreadItem(thread = pairs.first,
                users = pairs.second,
                navHostController,
                FirebaseAuth.getInstance().currentUser!!.uid )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun HomeView() {
//    Home()
}