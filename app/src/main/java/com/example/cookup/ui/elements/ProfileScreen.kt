package com.example.cookup.ui.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cookup.viewmodel.FavoritesViewModel
import com.example.cookup.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(navController: NavHostController,
                  favoritesViewModel: FavoritesViewModel) {
    val profileViewModel: ProfileViewModel = viewModel()
    val meals = profileViewModel.mealsList

    var showForm by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (!showForm) {
            Text("Welcome to your profile!", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            // Кнопка для відкриття форми
            Button(
                onClick = { showForm = true },
                modifier = Modifier.fillMaxWidth().height(35.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary.copy(0.1f),
                    contentColor = MaterialTheme.colorScheme.onPrimary),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
            ) {
                Text("Add Your Recipe", style = MaterialTheme.typography.bodySmall)
            }
            if(meals.isEmpty()){
                Text("You haven`t added any recipes yet..", style = MaterialTheme.typography.bodyMedium)
            }else{
                Text(meals[0].strMeal)
            }

        } else {
            // Форма додавання рецепта
            AddRecipeForm(
                onSubmit = { meal ->
                    println("Recipe submitted: $meal")
                    showForm = false
                },
                onCancel = { showForm = false },
                profileViewModel
            )
        }
    }
}
