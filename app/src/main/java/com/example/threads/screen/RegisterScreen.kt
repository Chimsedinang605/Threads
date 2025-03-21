package com.example.threads.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.threads.R
import com.example.threads.navigation.Routes
import com.example.threads.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScresn( navController: NavController  ) {

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
//
//    var imageUri by remember { mutableStateOf<Uri?>(null) }
//
//    val permissionToRequest =
//        if (
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
//            android.Manifest.permission.READ_MEDIA_IMAGES
//        }else{
//            android.Manifest.permission.READ_EXTERNAL_STORAGE
//        }

    val context = LocalContext.current

    val authViewModel : AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)
//
//    val launcher =
//        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
//           uri: Uri? ->
//            imageUri = uri
//        }
//
//    val permissionlauncher =
//        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
//            isGranted: Boolean ->
//
//            if (isGranted) {
//
//            } else {
//
//            }
//    }

    LaunchedEffect(firebaseUser) {

        if( firebaseUser != null) {
            navController.navigate(Routes.BottomNav.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    val gradientColors = listOf(

//        Color(0xFFFFF9F1),
//        Color(0xFFF0E6FF), // Light purple at top
//        Color(0xFFEAF6FF), // Light blue in middle
//        Color(0xFFE8FFF0),
//        Color(0xFFE5FFED)
        // Light green at bottom
        Color(0xFFF8F8FF), // Very Light Gray
        Color(0xFFE6E6FA), // Light Lavender
        Color(0xFFF0F8FF)  // Alice Blue
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(colors = gradientColors)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Spacer(modifier = Modifier.height(50.dp))

            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Register",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color(0xFF4285F4),
                    fontFamily = FontFamily.Serif

                )

//                Image(
//                    painter = if (imageUri == null) painterResource(id = R.drawable.person)
//                    else rememberAsyncImagePainter(imageUri),
//                    contentDescription = "Thread Logo"
//                    ,modifier = Modifier
//                        .size(100.dp)
//                        .clip(CircleShape)
//                        .background(Color.White)
//                        .clickable {
//
//                            // handle image click
//                            val isGranted = ContextCompat.checkSelfPermission(
//                                context, permissionToRequest
//                            ) == PackageManager.PERMISSION_GRANTED
//
//                            if (isGranted) {
//                                launcher.launch("image/*")
//                            } else {
//                                permissionlauncher.launch(permissionToRequest)
//                            }
//
//
//                        },
//                    contentScale = ContentScale.Crop)


                Spacer(modifier = Modifier.height(40.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = {name = it },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "accout icon",
                            tint = Color(0xFF4285F4)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    label = {Text("Name", color = Color.Gray)},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Blue,
                        unfocusedTextColor = Color.Gray,
                        focusedIndicatorColor = Color.Blue,
                        unfocusedIndicatorColor = Color.LightGray,
                        cursorColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true

                )

                OutlinedTextField(
                    value = username,
                    onValueChange = {username = it },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "accout icon",
                            tint = Color(0xFF4285F4)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    label = {Text("Username", color = Color.Gray)},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Blue,
                        unfocusedTextColor = Color.Gray,
                        focusedIndicatorColor = Color.Blue,
                        unfocusedIndicatorColor = Color.LightGray,
                        cursorColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true

                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email icon",
                            tint = Color(0xFF4285F4)
                        ) },
                    label = { Text("Email",
                        color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),

                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Blue,
                        unfocusedTextColor = Color.Gray,
                        focusedIndicatorColor = Color.Blue,
                        unfocusedIndicatorColor = Color.LightGray,
                        cursorColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                 // Password input field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = Color.Gray) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    leadingIcon = {Icon(
                        painter = painterResource(
                            id = R.drawable.password_icon
                        ),
                        contentDescription = "Password icon",
                        tint = Color(0xFF0095F6)
                    )},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 1.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Blue,
                        unfocusedTextColor = Color.Gray,
                        focusedIndicatorColor = Color.Blue,
                        unfocusedIndicatorColor = Color.LightGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Log in button
                ElevatedButton(
                    onClick = {
                        when {
                            name.isEmpty() -> {
                                Toast.makeText(context, "Vui lòng nhập tên.", Toast.LENGTH_SHORT).show()
                            }
                            email.isEmpty() -> {
                                Toast.makeText(context, "Vui lòng nhập email.", Toast.LENGTH_SHORT).show()
                            }
                            password.isEmpty() -> {
                                Toast.makeText(context, "Vui lòng nhập mật khẩu.", Toast.LENGTH_SHORT).show()
                            }

                            else -> {
                                authViewModel.register(name, username, email, password, context)
                            }
                        }
                    }
                    ,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor =  Color(0xFF0095F6)
                    ),
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Text(
                        text = "Sign Up",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif
                    )
                }

//                fb
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    HorizontalDivider(modifier = Modifier.weight(1f))
                    Text(
                        text = "Or",
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    //Facebook Button
                    OutlinedButton (
                        onClick = { /*logic */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(32.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White
                        ),
                        border = BorderStroke(1.dp,
                            Color.LightGray),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {


                        Row (
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                painter = painterResource(id  = com.example.threads.R.drawable.facebook),
                                contentDescription = "Facebook",
                                tint = Color(0xFF1877F2),
                                modifier = Modifier.size(18.dp)

                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Facebook",
                                color = Color.Gray,
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Serif
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))

                    // Google
                    OutlinedButton(
                        onClick = { /*logic */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(32.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White
                        ),
                        border = BorderStroke(1.dp, Color.LightGray),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Row (
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.gg),
                                contentDescription = "Google",
                                tint = Color.Unspecified ,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Google",
                                color = Color.Gray,
                                fontSize = 18.sp,
                                fontFamily = FontFamily.Serif
                            )
                        }
                    }

            }


            }
//            Spacer(modifier = Modifier.height(50.dp))


            Row(
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
//                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Do you have account?",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                TextButton(onClick = {


                    navController.navigate(Routes.LoginScreen.routes)
                    {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }


                },
                    modifier = Modifier.offset(x = (-5).dp)
                ) {
                    Text(
                        text = "Login",
                        fontSize = 14.sp,
                        color = Color(0xFF3B5AF5),
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Default
                    )
                }
            }

        }


    }

}

@Preview(showBackground = true)
@Composable
fun SignUpView(){
//    RegisterScresn()

}