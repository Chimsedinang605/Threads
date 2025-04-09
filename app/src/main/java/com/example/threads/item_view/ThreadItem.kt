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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.*
import coil.compose.rememberAsyncImagePainter
import com.example.threads.Data.SharePref
import com.example.threads.R

@Composable
fun ThreadItem(){

    ConstraintLayout (modifier = Modifier.fillMaxWidth().padding(16.dp)){
        val context = LocalContext.current
        val (userImage, userName, date, time, title, image) = createRefs()
        Image(
            painter = rememberAsyncImagePainter(model = SharePref.getImageUrl(context)),
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

        Text(
            text = "user name",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.constrainAs(userName) {
                top.linkTo(userImage.top)
                start.linkTo(userImage.end, margin = 12.dp)
                bottom.linkTo(userImage.bottom)
            }
        )

        Text(
            text = "Title",
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(userName.bottom)
                start.linkTo(userName.start)
            }
        )

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
@Preview(showBackground = true)
@Composable
fun ShowThreadList(){
    ThreadItem()
}