package com.example.cookup.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cookup.R
import com.example.cookup.viewmodel.MealSearchViewModel

// Екран для показу знайдених за пошуковим запитом рецептів
@Composable
fun SearchedMealsScreen(query: String, navController: NavHostController) {
    val viewModel: MealSearchViewModel = viewModel()
    LaunchedEffect(query) {
        viewModel.searchMeals(query)
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
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .padding(8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    item(span = { GridItemSpan(2) }) {
                        Row(
                            modifier = Modifier.padding(0.dp).heightIn(max = 30.dp)
                        ) {
                            Text(
                                text = "Found by query: $query",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp),
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                    items(meals) { meal ->
                        MealCard(meal) { idMeal ->
                            navController.navigate("mealDetail/$idMeal")
                        }
                    }
                }
            }
        }
    }
}