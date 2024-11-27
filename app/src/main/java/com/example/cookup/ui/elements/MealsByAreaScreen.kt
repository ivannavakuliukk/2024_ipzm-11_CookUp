package com.example.cookup.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cookup.viewmodel.AreaMealsViewModel
import com.example.cookup.viewmodel.FavoritesViewModel

@Composable
fun MealsByAreaScreen(
    area: String,
    navController: NavHostController,
    favoritesViewModel: FavoritesViewModel
) {
    val viewModel: AreaMealsViewModel = viewModel()
    // Завантажуємо страви за категорією
    LaunchedEffect(area) {
        viewModel.loadMealsByArea(area, favoritesViewModel.favoriteIds)
    }
    LaunchedEffect(favoritesViewModel.favoriteIds){
        viewModel.syncWithFavorites(favoritesViewModel.favoriteIds)
    }
    val meals = viewModel.mealsList
    if (meals.isEmpty()) {
        Box(contentAlignment = Alignment.Center,) {
            CircularProgressIndicator()
        }
    } else {
        MealGrid(meals, navController, area, iconOn = true, favoritesViewModel)
    }
}