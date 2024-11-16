package com.example.cookup.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cookup.R
import com.example.cookup.viewmodel.AreaMealsViewModel

@Composable
fun MealsByAreaScreen(area: String, navController: NavHostController) {
    val viewModel: AreaMealsViewModel = viewModel()
    // Завантажуємо страви за категорією
    LaunchedEffect(area) {
        viewModel.loadMealsByArea(area)
    }
    val meals = viewModel.mealsList
    if (meals.isEmpty()) {
        Box(contentAlignment = Alignment.Center,) {
            CircularProgressIndicator()
        }
    } else {
        MealGrid(meals, navController, area, iconOn = true)
    }
}