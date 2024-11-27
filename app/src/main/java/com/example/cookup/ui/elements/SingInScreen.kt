package com.example.cookup.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cookup.R
import com.example.cookup.ui.theme.Poppins

// Компонент для відображення екрану входу
@Composable
fun SignInScreen( onSignInClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val topPadding = screenHeight * 0.2f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color=Color.White)
    ) {
        Column(
            modifier = Modifier.padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "CookUp icon",
                modifier = Modifier
                    .size(80.dp)
            )
            Text(
                text = "Welcome to\nCookUp",
                style = TextStyle(
                    fontFamily = Poppins,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.15.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .padding(top = topPadding, bottom = 8.dp),
                color = Color(0xFFFD5D69),
            )
            // Кнопка для входу з Google
            Button(
                onClick  = onSignInClick, // Виклик функції входу
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC6C9))
            ) {
                // Текст на кнопці
                Text(text = "Sign in with Google", color = Color(0xFFFD5D69))
            }
        }
    }
}