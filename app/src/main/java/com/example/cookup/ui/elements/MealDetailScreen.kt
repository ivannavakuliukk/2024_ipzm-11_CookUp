package com.example.cookup.ui.elements


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cookup.data.models.Meal
import com.example.cookup.viewmodel.MealDetailViewModel
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.cookup.R

// Компонент для відображення детального екрану рецепту
@Composable
fun MealDetailScreen(idMeal: String, navController: NavHostController) {
    val viewModel: MealDetailViewModel = viewModel()
    val mealDetail by viewModel.mealDetail
    // Завантажити деталі страви за ідентифікатором, коли цей Composable створено вперше
    LaunchedEffect(idMeal) {
        viewModel.fetchMealById(idMeal)
    }

    // Якщо страва завантажена, відобразити її
    mealDetail?.let { DisplayMealDetails(it, navController) }
}

@Composable
fun DisplayMealDetails(meal: Meal, navController: NavHostController) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(26.dp).padding(0.dp).padding(start = 0.dp)
                )
            }
            Text(
                text = meal.strMeal, // Назва страви
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Image(
            painter = rememberAsyncImagePainter(meal.strMealThumb),
            contentDescription = meal.strMeal,
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Category: ${meal.strCategory}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(text = "Region: ${meal.strArea}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Ingredients:", style = MaterialTheme.typography.bodyMedium)
        // Відображення інгредієнтів і їх кількості
        meal.ingredients.zip(meal.measures).forEach { (ingredient, measure) ->
            if (ingredient.isNotEmpty() && measure.isNotEmpty()) {
                Text(
                    text = "$ingredient - $measure",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Instructions:", style = MaterialTheme.typography.bodyMedium)
        Text(text = meal.strInstructions, style = MaterialTheme.typography.bodySmall)

    }
}