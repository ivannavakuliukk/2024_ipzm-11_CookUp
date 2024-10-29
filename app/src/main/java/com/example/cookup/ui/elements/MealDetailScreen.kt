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
import com.example.cookup.models.Meal
import com.example.cookup.viewmodel.MealDetailViewModel
import coil.compose.rememberImagePainter
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue

// Компонент для відображення детального екрану рецепту
@Composable
fun MealDetailScreen(idMeal: String, viewModel: MealDetailViewModel = viewModel()) {
    val mealDetail by viewModel.mealDetail

    // Завантажити деталі страви за ідентифікатором, коли цей Composable створено вперше
    LaunchedEffect(idMeal) {
        viewModel.fetchMealById(idMeal)
    }

    // Якщо страва завантажена, відобразити її
    mealDetail?.let { displayMealDetails(it) }
}

@Composable
fun displayMealDetails(meal: Meal) {
    Column(modifier = Modifier
        .padding(16.dp)
        .verticalScroll(rememberScrollState())) {
    Text(text = meal.strMeal, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = rememberImagePainter(meal.strMealThumb),
            contentDescription = meal.strMeal,
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Category: ${meal.strCategory}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Region: ${meal.strArea}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Ingredients:", style = MaterialTheme.typography.bodyMedium)
        // Відображення інгредієнтів і їх кількості
        meal.ingredients.zip(meal.measures).forEach { (ingredient, measure) ->
            if (ingredient.isNotEmpty() && measure.isNotEmpty()) {
                Text(text = "$ingredient - $measure")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Instructions:\n${meal.strInstructions}", style = MaterialTheme.typography.bodyMedium)
    }
}