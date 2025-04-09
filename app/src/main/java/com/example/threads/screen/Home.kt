package com.example.threads.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.threads.item_view.ThreadItem

@Composable
fun Home(){
    LazyColumn {
        items(30) {
            ThreadItem()
        }


    }
}

@Preview(showBackground = true)
@Composable
fun HomeView() {
    Home()
}