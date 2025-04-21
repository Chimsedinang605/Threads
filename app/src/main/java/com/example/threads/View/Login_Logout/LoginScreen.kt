package com.example.threads.View.Login_Logout

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
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
fun LoginScreen(navController: NavController) {
    var emailOrUsername by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    LaunchedEffect(firebaseUser) {

        if( firebaseUser != null) {
            navController.navigate(Routes.BottomNav.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }


    val gradientColors = listOf(
        Color(0xFFF8F8FF), // Very Light Gray
        Color(0xFFE6E6FA), // Light Lavender
        Color(0xFFF0F8FF)  // Alice Blue

//        Color(0xFFFFF9F1),
//        Color(0xFFF0E6FF), // Light purple at top
//        Color(0xFFEAF6FF), // Light blue in middle
//        Color(0xFFE8FFF0),
//        Color(0xFFE5FFED)
    // Light green at bottom
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = gradientColors
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Middle section with logo and inputs
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                //  logo with gradient
                Box(
                    modifier = Modifier
                        .size(190.dp)
                        .padding(bottom = 40.dp)
                ) {
                    // This is a placeholder for Instagram's gradient logo
                    // In a real app, you would use an actual resource with the right colors
                    Image(
                        painter = painterResource(id = R.drawable.ig), // Placeholder
                        contentDescription = "Thread Logo",
                        modifier = Modifier.fillMaxSize()
//                            .clip(CircleShape)
                    )
                }

                Text(
                    text = "Sign In",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color(0xFF3B5AF5)
                )

                Spacer(modifier = Modifier.height(30.dp))
                // Username/email input field
                OutlinedTextField(
                    value = emailOrUsername,
                    onValueChange = { emailOrUsername = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    leadingIcon = {Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Persion icon",
                        tint = Color(0xFF0095F6)
                    )},
                    label = { Text("Email or mobile number",
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

//                Spacer(modifier = Modifier.height(4.dp))

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
                        tint = Color.Unspecified
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

//                 Forgot pass
                Row(
                    modifier = Modifier.fillMaxWidth(), // Đảm bảo Row chiếm toàn bộ chiều rộng
                    horizontalArrangement = Arrangement.End // Căn chỉnh các thành phần con sang phải
                ) {
                    TextButton(
                        onClick = {
                        /* Logic */
                        navController.navigate(Routes.ForgetScreen.routes)
                        {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = "Forget password?",
                            fontSize = 14.sp,
                            color = Color.Gray,
                        )
                    }
                }
//
                Spacer(modifier = Modifier.height(20.dp))

                // Log in button
                ElevatedButton(onClick = {
//                    logic
                    when {
                        emailOrUsername.isEmpty() -> {
                            Toast.makeText(context, "Vui lòng nhập email hoặc username.", Toast.LENGTH_SHORT).show()
                        }
                        password.isEmpty() -> {
                            Toast.makeText(context, "Vui lòng nhập mật khẩu.", Toast.LENGTH_SHORT).show()
                        }

                        else -> {
                            authViewModel.login( emailOrUsername, password)
                        }
                    }

                },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor =  Color(0xFF0095F6)
                    ),
                    shape = RoundedCornerShape(32.dp)
                    ) {
                    Text(
                        text = "Log In",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                // Or divider

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
                                painter = painterResource(id  = R.drawable.facebook),
                                contentDescription = "Facebook",
                                tint = Color(0xFF1877F2),
                                modifier = Modifier.size(18.dp)

                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Facebook",
                                color = Color.Gray,
                                fontSize = 16.sp
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
                                fontSize = 18.sp
                            )
                        }
                    }
                }


            }
            // Or divider



//            Spacer(modifier = Modifier.height(50.dp))


            Row(
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
//                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have account?",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                TextButton(onClick = {

                    navController.navigate(Routes.RegisterScreen.routes)
                    {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }

                },
                    modifier = Modifier.offset(x = (-5).dp)
                ) {
                    Text(
                        text = "Create account",
                        fontSize = 14.sp,
                        color = Color(0xFF3B5AF5),
                        fontWeight = FontWeight.Medium
                    )
                }
            }



        }
    }


}

@Preview(showBackground = true)
@Composable
fun LoginView(){
//    LoginScreen()
}
