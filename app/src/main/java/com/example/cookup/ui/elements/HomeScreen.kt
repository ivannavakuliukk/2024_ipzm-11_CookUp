package com.example.cookup.ui.elements


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cookup.viewmodel.MealViewModel

// Компонент для головного екрану
@Composable
fun HomeScreen(navController: NavHostController) {
    val mealViewModel: MealViewModel = viewModel() // Отримуємо ViewModel для роботи з даними страв
    val meals = mealViewModel.mealsList

    // Показуємо індикатор завантаження, поки список порожній
    if (meals.isEmpty()) {
        // Індикатор завантаження
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
        }
    } else {
        MealGrid(meals, navController, "Recommended recipes", iconOn = false)
    }
}