package com.example.threads.item_view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.*
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threads.R
import com.example.threads.model.ThreadModel
import com.example.threads.model.UserModel

@Composable
fun ThreadItem(
    thread: ThreadModel,
    users: UserModel,
    navController: NavHostController,
    userId : String
){
    Column {

        ConstraintLayout (modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp)){
            val (userImage, userName, date, time, title, image) = createRefs()
            // avt
            Image(
                painter = rememberAsyncImagePainter(model = users.imgUrl),
                contentDescription = "avt",
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .constrainAs(userImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .size(36.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            // ten nguoi dung
            Text(
                text = users.name,
                style = MaterialTheme.typography.titleMedium, // Sử dụng style từ MaterialTheme
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier.constrainAs(userName) {
                    top.linkTo(userImage.top)
                    start.linkTo(userImage.end, margin = 12.dp)
                    bottom.linkTo(userImage.bottom)
                }
            )
            // thread noi dung
            Text(
                text = thread.thread,
                fontSize = 18.sp,
                color = Color.DarkGray,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(userName.bottom)
                    start.linkTo(userName.start)
                }
            )

            if (thread.image != "" ){
                Card ( modifier = Modifier.constrainAs(image){
                    top.linkTo(title.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }){
                    Image(
                        painter = painterResource(id = R.drawable.faker),
                        contentDescription = "image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

        }

        HorizontalDivider(
            color = Color.LightGray,
            thickness = 1.dp
        )
    }

}


@Preview(showBackground = true)
@Composable
fun ShowThreadList(){
//    ThreadItem()
}