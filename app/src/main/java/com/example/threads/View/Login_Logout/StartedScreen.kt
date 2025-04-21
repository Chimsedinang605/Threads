package com.example.threads.View.Login_Logout

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.threads.R
import com.example.threads.navigation.Routes

@Composable
fun StartedScreen(navController: NavController) {
    Column (modifier = Modifier.fillMaxWidth()){

        Box(modifier = Modifier.fillMaxWidth()
            .background(Color(0xFFF7F7F7))
            .weight(2f),
            contentAlignment = Alignment.Center) {
            Image(painter = painterResource(id = R.drawable.pattern),
                contentDescription = "banner",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
                .background(Color(0xFFF7F7F7),
                    shape = RoundedCornerShape(12.dp)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Add the Get Started button
            Button(
                onClick = { /* Handle button click */

                    navController.navigate(Routes.LoginScreen.routes)
                    {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }

                },
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(64.dp)
                    .shadow(elevation = 3.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(Color.White)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Get Started",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(start = 16.dp)
                    )

                    Image(painter = painterResource(id = R.drawable.ig),
                        contentDescription = "Instagram logo",
                        modifier = Modifier.size(25.dp)
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowStart() {
//    StartedScreen()
}