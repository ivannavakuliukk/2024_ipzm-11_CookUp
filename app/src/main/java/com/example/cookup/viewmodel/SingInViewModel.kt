package com.example.cookup.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cookup.data.repository.SignInRepository
import com.google.android.gms.auth.api.signin.GoogleSignInClient


class SignInViewModel(
    private val repository: SignInRepository,
    private val googleSignInClient: GoogleSignInClient
) : ViewModel() {

    private val _signInState = MutableLiveData<Result<Boolean>>()
    val signInState: LiveData<Result<Boolean>> = _signInState

    // Перевірка, чи користувач уже авторизований
    fun checkUserSignedIn(): Boolean = repository.getCurrentUser() != null

    // Отримання Google Sign-In intent
    fun getGoogleSignInIntent(): Intent = repository.getGoogleSignInIntent(googleSignInClient)

    // Обробка результату входу через Google
    fun handleSignInResult(data: Intent?) {
        repository.handleGoogleSignInResult(data) { result ->
            _signInState.postValue(result)
        }
    }
}
