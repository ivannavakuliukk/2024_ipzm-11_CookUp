package com.example.cookup.data.datasource

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

// Клас, який забезпечує доступ до Firebase
class SignInDataSource(private val auth: FirebaseAuth) {

    // Перевірка, чи вже є авторизований користувач
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    // Вхід за допомогою Firebase через облікові дані Google
    fun signInWithCredential(
        credential: AuthCredential,
        onComplete: (success: Boolean, error: String?) -> Unit
    ) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }
}
