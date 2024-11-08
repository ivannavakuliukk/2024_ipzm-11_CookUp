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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                Row(modifier = Modifier.padding(0.dp).heightIn(max = 30.dp)
                ){
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(28.dp).padding(0.dp).padding(bottom = 3.dp)
                        )
                    }
                    Text(
                        text = area,
                        style = MaterialTheme.typography.bodyLarge,
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