package com.example.threads.View

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.threads.R
import com.example.threads.item_view.UserItem
import com.example.threads.viewmodel.SearchViewModel


@Composable
fun Search(navHostController: NavHostController){

    val searchViewModel: SearchViewModel = viewModel()
    val userList by searchViewModel.userList.observeAsState()
    var search by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tìm kiếm",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Instagram-style search bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFF2F2F2)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                BasicTextField(
                    value = search,
                    onValueChange = { search = it },
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.Black,
                        fontSize = 16.sp
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) { innerTextField ->
                    Box {
                        if (search.isEmpty()) {
                            Text(
                                text = "Tìm kiếm người dùng",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (search.isNotEmpty()) {
            Text(
                text = "Kết quả tìm kiếm",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        LazyColumn {
            if (userList != null && userList!!.isNotEmpty()) {
                val filterItems = if (search.isEmpty()) {
//                    emptyList()  // Don't show anything when search is empty
                    userList!!
                } else {
                    userList!!.filter {
                        it.name?.contains(search, ignoreCase = true) == true ||
                        it.username?.contains(search, ignoreCase = true) == true
                    }
                }

                items(filterItems) { user ->
                    UserItem(
                        user,
                        navHostController)

                }

                if (search.isNotEmpty() && filterItems.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Không tìm thấy kết quả nào",
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }

}