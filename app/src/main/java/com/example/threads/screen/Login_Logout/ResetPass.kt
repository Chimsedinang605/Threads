package com.example.threads.screen.Login_Logout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.threads.R
import com.example.threads.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPass(navController: NavController){

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }


    val gradientColors = listOf(

        Color(0xFFFFF9F1),
        Color(0xFFF0E6FF), // Light purple at top
        Color(0xFFEAF6FF), // Light blue in middle
        Color(0xFFE8FFF0),
        Color(0xFFE5FFED)
        // Light green at bottom
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
//                brush = Brush.verticalGradient(
//                    colors = gradientColors
//                )
                Color.White
            )
    ) {

        IconButton(
            onClick = {
                navController.navigate(Routes.ForgetScreen.routes) {
                    popUpTo(navController.graph.findStartDestination().id){
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true

                }
            },
            modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
        ){
            Icon(
                painter = painterResource(id = R.drawable.back_icon),
                contentDescription = "back icon"
            )
        }

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
            , horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Spacer(modifier = Modifier.height(130.dp))

            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 5.dp)
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.reset_safety_icon),
                        contentDescription = "password ",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Create a new Password",
                    fontFamily = FontFamily.Serif,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF3B5AF5),
                    modifier = Modifier.padding(bottom = 80.dp)
                )

//                    Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {password = it},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
//                    leadingIcon = { Icon(
//                        painter = painterResource(id = R.drawable.password_icon),
//                        contentDescription = "password icon",
//                        tint = Color(0xFF0095F6)
//                    ) },
                    label = {Text("Password", color = Color.Gray)},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),

                    )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {confirmPassword = it},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
//                    leadingIcon = { Icon(
//                        painter = painterResource(id = R.drawable.password_icon),
//                        contentDescription = "password icon",
//                        tint = Color(0xFF0095F6)
//                    ) },
                    label = {Text("Confirm Password", color = Color.Gray)},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),

                    )

                Spacer(modifier = Modifier.height(20.dp))
                ElevatedButton(onClick = {
//                    logic
                },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor =  Color(0xFF0095F6)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Submit",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif
                    )
                }

            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun ResetView(){
//    ResetPass()

}