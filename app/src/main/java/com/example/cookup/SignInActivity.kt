package com.example.cookup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cookup.data.datasource.SignInDataSource
import com.example.cookup.data.repository.SignInRepository
import com.example.cookup.ui.elements.SignInScreen
import com.example.cookup.viewmodel.SignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

// Активність авторизації користувача
class SignInActivity : ComponentActivity() {

    private lateinit var viewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ініціалізація FirebaseAuth, GoogleSignInClient, DataSource, Repository, ViewModel
        val auth = FirebaseAuth.getInstance()
        val googleSignInClient = GoogleSignIn.getClient(
            this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )
        val dataSource = SignInDataSource(auth)
        val repository = SignInRepository(dataSource)
        viewModel = SignInViewModel(repository, googleSignInClient)

        // Якщо користувач авторизований, переходимо на MainActivity
        if (viewModel.checkUserSignedIn()) {
            navigateToMainActivity()
            return
        }
        // Налаштування UI
        setContent {
            SignInScreen(
                onSignInClick = { startActivityForResult(viewModel.getGoogleSignInIntent(), RC_SIGN_IN) }
            )
        }
        // Спостереження за станом входу
        viewModel.signInState.observe(this) { result ->
            result.onSuccess {
                navigateToMainActivity()
            }.onFailure { error ->
                showErrorToast("Error log in: ${error.message}")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            viewModel.handleSignInResult(data)
        }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
