package com.example.cookup.ui.elements


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cookup.R
import com.example.cookup.viewmodel.FavoritesViewModel
import com.example.cookup.viewmodel.MealViewModel

// Компонент для головного екрану
@Composable
fun HomeScreen(navController: NavHostController, favoritesViewModel: FavoritesViewModel) {
    val mealViewModel: MealViewModel = viewModel() // Отримуємо ViewModel для роботи з даними страв
    val meals = mealViewModel.mealsList
    mealViewModel.setFavoriteIds(favoritesViewModel.favoriteIds)
    LaunchedEffect(favoritesViewModel.favoriteIds) {
        mealViewModel.syncWithFavorites2(favoritesViewModel.favoriteIds)
    }

    val lazyListState = rememberSaveable(saver = LazyGridState.Saver) { LazyGridState() }
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex + lazyListState.layoutInfo.visibleItemsInfo.size >= lazyListState.layoutInfo.totalItemsCount }
            .collect { isAtEnd ->
                if (isAtEnd) {
                    mealViewModel.setFavoriteIds(favoritesViewModel.favoriteIds)
                    mealViewModel.fetchRandomMeals()
                }
            }
    }
    // Показуємо індикатор завантаження, поки список порожній
    if (mealViewModel.isFirstLoading || mealViewModel.isSynchronized) {
        // Індикатор завантаження
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(8.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            state = lazyListState
        ) {
            item(span = { GridItemSpan(2) }) {
                Button(
                    onClick = {  navController.navigate("users_meals") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary.copy(0.1f),
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Text(text = "View Author`s recipes", style = MaterialTheme.typography.bodySmall)
                }
            }
            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier.padding(0.dp).heightIn(max = 25.dp)
                ) {
                    Text(
                        text = "Recommended recipes",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
            items(meals) { meal -> // Проходимо по списку страв
                val isFavorite = remember { mutableStateOf(meal.isFavorite) }
                MealFavoriteCard(
                    meal = meal,
                    isFavorite = isFavorite,
                    onFavoriteClick = {
                        if (isFavorite.value) {
                            favoritesViewModel.removeRecipeFromFavorites(meal.idMeal)
                        } else {
                            favoritesViewModel.addRecipeToFavorites(meal.idMeal)
                        }
                        isFavorite.value = !isFavorite.value
                        Log.d("Favorite", "I changed")
                    }
                ) { idMeal ->
                    // Навігація до MealDetailScreen
                    navController.navigate("mealDetail/$idMeal")
                }

            }
            if (mealViewModel.isLoading) {
                item(span = { GridItemSpan(2) }) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }
    }
}