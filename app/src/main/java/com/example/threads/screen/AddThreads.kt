package com.example.threads.screen

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.constraintlayout.compose.*
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.threads.Data.*
import com.example.threads.R
import com.example.threads.viewmodel.AuthViewModel

@Composable
fun AddThreads(){
    ConstraintLayout(modifier = Modifier.fillMaxSize().padding(16.dp).background(Color.White)) {

        val context = LocalContext.current
        var thread by remember { mutableStateOf("") }
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        // uses-permission
        val permissionToRequest =
            if (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                android.Manifest.permission.READ_MEDIA_IMAGES
            }else{
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            }

        val authViewModel : AuthViewModel = viewModel()
        val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

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
                    // logic close

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
            painter = rememberAsyncImagePainter(model = SharePref.getImageUrl(context)),
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

@Preview(showBackground = true)
@Composable
fun AddPostView() {
    AddThreads()
}