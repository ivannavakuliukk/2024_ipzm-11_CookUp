package com.example.cookup.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cookup.viewmodel.FavoritesViewModel
import com.example.cookup.viewmodel.MealSearchViewModel

// Екран для показу знайдених за пошуковим запитом рецептів
@Composable
fun SearchedMealsScreen(
    query: String,
    navController: NavHostController,
    favoritesViewModel: FavoritesViewModel
) {
    val viewModel: MealSearchViewModel = viewModel()
    LaunchedEffect(query) {
        viewModel.searchMeals(query, favoritesViewModel.favoriteIds)
    }
    val meals = viewModel.mealsList
    val isLoading = viewModel.isLoading
    val isEmpty = meals.isEmpty() && !isLoading
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }
            isEmpty -> {
                Text(
                    text = "No meals found by query $query",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            else -> {
                MealGrid(
                    meals,
                    navController,
                    "Meals found by query: $query",
                    iconOn = false,
                    favoritesViewModel
                )
            }
        }
    }
}