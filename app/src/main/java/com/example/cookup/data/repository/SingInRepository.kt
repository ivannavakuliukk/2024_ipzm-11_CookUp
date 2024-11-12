package com.example.cookup.data.repository

import android.content.Intent
import com.example.cookup.data.datasource.SignInDataSource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

/*
Репозиторій SingIn - містить логіку для роботи з даними,
Забезпечує вхід через Google, викликає SingInDataSource для доступу до
Firebase.
 */
class SignInRepository(private val dataSource: SignInDataSource) {

    // Перевірка, чи користувач уже авторизований
    fun getCurrentUser() = dataSource.getCurrentUser()

    // Отримання Google Sign-In intent
    fun getGoogleSignInIntent(googleSignInClient: GoogleSignInClient): Intent {
        return googleSignInClient.signInIntent
    }

    // Обробка результату Google Sign-In та авторизація через Firebase
    fun handleGoogleSignInResult(
        data: Intent?,
        onComplete: (Result<Boolean>) -> Unit
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.let {
                signInWithGoogle(it, onComplete)
            } ?: onComplete(Result.failure(Exception("Sign-In account is null")))
        } catch (e: ApiException) {
            onComplete(Result.failure(e))
        }
    }

    // Виконання авторизації через Firebase з обліковим записом Google
    private fun signInWithGoogle(account: GoogleSignInAccount, onComplete: (Result<Boolean>) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        dataSource.signInWithCredential(credential) { success, error ->
            if (success) {
                onComplete(Result.success(true))
            } else {
                onComplete(Result.failure(Exception(error)))
            }
        }
    }
}
