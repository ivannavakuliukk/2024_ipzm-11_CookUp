package com.example.cookup.ui.elements

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cookup.viewmodel.MealViewModel

// Компонент для головного екрану
@Composable
fun HomeScreen(navController: NavHostController) {
    val mealViewModel: MealViewModel = viewModel() // Отримуємо ViewModel для роботи з даними страв
    // викликаємо метод для вибору 30 рандомних страв
    val meals = mealViewModel.mealsList
    // Показуємо індикатор завантаження, поки список порожній
    if (meals.isEmpty()) {
        // Індикатор завантаження
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
        }
    } else {
        // Якщо страви завантажені, показуємо UI
        Column(modifier = Modifier.padding(1.dp)) {
//            Text(
//                text = "Recommended Recipes",
//                style = MaterialTheme.typography.bodyMedium,
//                modifier = Modifier.padding(vertical = 1.dp, horizontal = 14.dp)
//            )
            // Створюємо сітку для відображення страв
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(8.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(meals) { meal -> // Проходимо по списку страв
                    MealCard(meal) { idMeal ->
                        // Навігація до MealDetailScreen
                        navController.navigate("mealDetail/$idMeal")
                    } // Відображаємо картку з інформацією про страву
                }
            }
        }
    }
}