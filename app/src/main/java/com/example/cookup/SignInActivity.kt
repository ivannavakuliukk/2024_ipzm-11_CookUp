package com.example.cookup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignInActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth // Firebase Authentication
    private lateinit var googleSignInClient: GoogleSignInClient // Google Sign-In Client

    // Request code for Google Sign-In
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ініціалізація FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Перевірка, чи є користувач вже авторизованим
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Якщо користувач авторизований, перенаправляємо на MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Завершити SignInActivity
            return // Виходимо з методу
        }

        // Налаштування Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Запит на отримання токена ідентифікації
            .requestEmail() // Запит на отримання електронної пошти
            .build()

        // Ініціалізація Google Sign-In клієнта
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Встановлюємо контент для SignInActivity
        setContent {
            SignInScreen() // Відображаємо екран входу
        }
    }

    // Функція для відображення екрану входу
    @Composable
    fun SignInScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .border(BorderStroke(2.dp, Color.Blue))
                    .padding(16.dp)
            ) {
                // Відображаємо значок Google
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(28.dp)
                )

                // Кнопка для входу з Google
                Button(
                    onClick = { signInWithGoogle() }, // Виклик функції входу
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF459DE4))
                ) {
                    // Текст на кнопці
                    Text(text = "Sign up with Google", color = Color.White)
                }
            }
        }
    }

    // Функція для входу через Google
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent // Отримуємо намір для входу
        startActivityForResult(signInIntent, RC_SIGN_IN) // Запускаємо активність для входу
    }

    // Обробка результату входу
    /*Цей метод не викликається явно, але він перевизначає стандартний метод onActivityResult
    з класу ComponentActivity. Android викликає цей метод автоматично після завершення діяльності,
     ініційованої через startActivityForResult
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Перевіряємо, чи є це результатом запиту на вхід через Google
        if (requestCode == RC_SIGN_IN) {
            // Отримуємо обліковий запис Google з результату
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Отримуємо успішно підписаний обліковий запис
                val account = task.getResult(ApiException::class.java)
                // Викликаємо метод для аутентифікації в Firebase з обліковим записом Google
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                showErrorToast("Не вдалось авторизуватись через Google${e.message}")
            }
        }
    }

    // Метод для аутентифікації в Firebase з обліковим записом Google
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        // Отримуємо облікові дані для входу в Firebase
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        // Виконуємо вхід в Firebase
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Успішний вхід
                    val user = auth.currentUser // Отримуємо поточного користувача
                    startActivity(Intent(this, MainActivity::class.java)) // Перенаправляємо на MainActivity
                    finish() // Завершуємо SignInActivity
                } else {
                    // Не вдалося увійти
                    showErrorToast("Не вдалося увійти")
                }
            }
    }


    // Метод для відображення Toast-повідомлень про помилки
    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}

