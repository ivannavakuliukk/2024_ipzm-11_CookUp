package com.example.cookup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    // Змінна для Firebase аутентифікації
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ініціалізація FirebaseAuth
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser // Отримання інформації про поточного користувача

        setContent {
            // Відображення екрану привітання з ім'ям користувача
            WelcomeScreen(currentUser?.displayName)
        }
    }

    // Composable функція для відображення екрану привітання
    @Composable
    fun WelcomeScreen(displayName: String?) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Показуємо Snack bar з привітанням
            val snackbarHostState = remember { SnackbarHostState() }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            // Показуємо привітання
            LaunchedEffect(displayName) {
                snackbarHostState.showSnackbar(
                    message = "Вітаємо, ${displayName ?: "Guest"}!",
                    duration = SnackbarDuration.Short // Змінюємо на короткий для автоматичного закриття
                )
            }
        }
    }
}

