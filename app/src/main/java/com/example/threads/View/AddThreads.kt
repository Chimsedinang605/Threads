package com.example.threads.View

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.constraintlayout.compose.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threads.Data.SharePref
import com.example.threads.R
import com.example.threads.navigation.Routes
import com.example.threads.viewmodel.AddThreadViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddThreads(navHostController: NavHostController ){

    ConstraintLayout(modifier = Modifier.fillMaxSize().padding(16.dp).background(Color.White)) {

        val threadViewModel: AddThreadViewModel = viewModel()
        val isPosted by threadViewModel.isPost.observeAsState(false)

        val context = LocalContext.current
        var thread by remember { mutableStateOf("") }

        var imageUri by remember { mutableStateOf<Uri?>(null) }
        var threadId by remember { mutableStateOf("") }


        // uses-permission
        val permissionToRequest =
            if (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                android.Manifest.permission.READ_MEDIA_IMAGES
            }else{
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            }
        val launcher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
                    uri: Uri? ->
                imageUri = uri
            }

        val permissionlauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
                    isGranted: Boolean ->

                if (isGranted) {

                } else {

                }
            }

        LaunchedEffect(isPosted ) {
            if (isPosted!!) {
                thread = ""
                imageUri = null
                Toast.makeText(context, "Thread adds", Toast.LENGTH_SHORT).show()

                navHostController.navigate(Routes.Home.routes) {

                    popUpTo(Routes.AddThread.routes) {
                        inclusive = true
                    }
                }
            }
        }

        // tao rang buoc cho constraintLayout
        val(closeIcon, title, avt, userName, editText, attachMedia, replyText, button, imageBox) = createRefs()

        Image(painter = painterResource(id = R.drawable.close), contentDescription = "Close",
            modifier = Modifier
                .size(24.dp)
                .constrainAs(closeIcon){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)

                }
                .clickable{

                    navHostController.navigate(Routes.Home.routes) {

                        popUpTo(Routes.AddThread.routes) {
                            inclusive = true
                        }
                    }

                }
        )

        //title
        Text(
            text = "New Thread",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.constrainAs(title) {
                                top.linkTo(closeIcon.top)
                                start.linkTo(closeIcon.end, margin = 16.dp)
            }
        )

        // avt user
        Image(
            painter =
             rememberAsyncImagePainter(model = SharePref.getImageUrl(context)),
            contentDescription = "avt",
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .constrainAs(avt) {
                    top.linkTo(title.bottom)
                    start.linkTo(parent.start)
                    width = Dimension.value(36.dp)
                    height = Dimension.value(36.dp)
                }
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Text(
//            text = "Nguyen hong",
            text = SharePref.getName(context),
            fontSize = 15.sp,
            color = Color.Black,
            modifier = Modifier.constrainAs(userName) {
                top.linkTo(avt.top)
                start.linkTo(avt.end, margin = 12.dp)
                bottom.linkTo(avt.bottom)
            }
        )

        BasicTextFieldWithHint( hint = "Start a thread...",value = thread ,
            onValueChange ={thread = it}
            ,modifier = Modifier.constrainAs(editText){
                        top.linkTo(userName.bottom)
                        start.linkTo(userName.start)
                        end.linkTo(parent.end)
            }.padding(horizontal = 8.dp, vertical = 8.dp).fillMaxWidth()
        )

        if (imageUri == null) {
            Image(painter = painterResource(id = R.drawable.attachment), contentDescription = "Add image",
                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(attachMedia){
                        top.linkTo(editText.bottom)
                        start.linkTo(editText.start)

                    }
                    .clickable{
                        // handle image click add new threads
                        val isGranted = ContextCompat.checkSelfPermission(
                            context, permissionToRequest
                        ) == PackageManager.PERMISSION_GRANTED

                        if (isGranted) {
                            launcher.launch("image/*")
                        } else {
                            permissionlauncher.launch(permissionToRequest)
                        }
                    }
            )
        } else {
            Box( modifier = Modifier.background(Color.Gray)
                .padding(1.dp)
                .constrainAs(imageBox) {
                    top.linkTo(editText.bottom)
                    start.linkTo(editText.start)
                    end.linkTo(parent.end)
                }
                .height(250.dp) ) {
                // thread image
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "Close",
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(),
//                        .clip(RectangleShape),
                    contentScale = ContentScale.Crop
                )
                Icon(imageVector = Icons.Default.Close, contentDescription = "Remove image",
                    modifier = Modifier.align(Alignment.TopEnd)
                        .clickable{
                            imageUri = null
                        })

            }
        }

        Text(
            text = "Anyone can reply", style = TextStyle (
            fontSize = 15.sp),
            modifier = Modifier.constrainAs(replyText) {
                start.linkTo(parent.start, margin = 12.dp)
                bottom.linkTo(parent.bottom, margin = 12.dp)
            }
        )
        TextButton(

            onClick = {

                if (imageUri == null ) {
                    threadViewModel.saveData(thread, "",
                        FirebaseAuth.getInstance().currentUser!!.uid  )
                }else {
                    threadViewModel.saveImage(thread, imageUri!!,
                        FirebaseAuth.getInstance().currentUser!!.uid )

                }

            },
            modifier = Modifier
                .constrainAs(button) {
                    end.linkTo(parent.end, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                }
                .padding(horizontal = 12.dp, vertical = 6.dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color(0xFF006BFF)
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Post",
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Đăng bài",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.25.sp
                    )
                )
            }
        }



    }

}

@Composable
fun BasicTextFieldWithHint(hint: String, value: String, onValueChange: (String) -> Unit,
                           modifier: Modifier) {
    Box(modifier = modifier.fillMaxWidth()) {
        if (value.isEmpty()) {
            Text(
                text = hint,
                color = Color.Gray
            )
        }
        BasicTextField(value = value, onValueChange = onValueChange,
            textStyle = TextStyle.Default.copy(color = Color.Black),
            modifier = Modifier.fillMaxWidth()
        )

    }
}

//@Preview(showBackground = true)
//@Composable
//fun AddPostView() {
//    AddThreads()
//}