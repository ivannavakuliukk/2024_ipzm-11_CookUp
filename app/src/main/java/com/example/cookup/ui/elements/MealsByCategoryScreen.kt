package com.example.cookup.ui.elements


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.cookup.viewmodel.CategoryMealsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cookup.viewmodel.FavoritesViewModel

@Composable
fun MealsByCategoryScreen(
    category: String,
    navController: NavHostController,
    favoritesViewModel: FavoritesViewModel
) {
    val viewModel: CategoryMealsViewModel = viewModel()
    // Завантажуємо страви за категорією
    LaunchedEffect(category) {
        viewModel.loadMealsByCategory(category, favoritesViewModel.favoriteIds)
    }
    val meals = viewModel.mealsList

    if (meals.isEmpty()) {
        Box(contentAlignment = Alignment.Center,modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
        }
    } else {
        MealGrid(meals, navController, category, iconOn = true, favoritesViewModel)
    }
}



