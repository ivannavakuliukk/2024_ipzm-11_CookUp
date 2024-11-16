package com.example.cookup.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cookup.viewmodel.FavoritesViewModel

@Composable
fun FavoritesScreen(navController: NavHostController) {
    val favoritesViewModel: FavoritesViewModel = viewModel()
    val meals = favoritesViewModel.mealsList
    val favoriteMealsState = favoritesViewModel.favoriteIds

    // Стани - завантаження та пустий список
    val isLoading = favoritesViewModel.isLoading
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
                    text = "You haven`t added any meals to favorite yet...",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Center).padding(horizontal = 14.dp),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            else -> {
                // Якщо страви завантажені, показуємо UI
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.padding(8.dp),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(meals) { meal -> // Проходимо по списку страв
                        val isFavorite =
                            rememberUpdatedState(favoriteMealsState.contains(meal.idMeal))
                        MealFavoriteCard(
                            meal = meal,
                            isFavorite = isFavorite.value,
                            onFavoriteClick = {
                                if (isFavorite.value) {
                                    favoritesViewModel.removeRecipeFromFavorites(meal.idMeal)
                                } else {
                                    favoritesViewModel.addRecipeToFavorites(meal.idMeal)
                                }
                            }
                        ) { idMeal ->
                            // Навігація до MealDetailScreen
                            navController.navigate("mealDetail/$idMeal")
                        }
                    }
                }
            }
        }
    }
}