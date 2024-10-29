package com.example.cookup

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cookup.viewmodel.MealViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import com.example.cookup.ui.elements.MealDetailScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cookup.ui.elements.MealCard

// Головна активність додатку
class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth // Змінна для роботи з Firebase Authentication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ініціалізація FirebaseAuth для роботи з обліковими записами
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser // Отримуємо інформацію про поточного користувача

        // Налаштування вмісту екрана
        setContent {
            val navController = rememberNavController() // Створюємо NavController для управління навігацією
            NavigationGraph(navController, currentUser?.displayName) // Викликаємо функцію навігації
        }
    }
}

// Функція навігації
@Composable
fun NavigationGraph(navController: NavHostController, userName: String?) {
    // Визначаємо основні маршрути навігації
    NavHost(navController = navController, startDestination = "mealList") {
        // Головний екран зі списком страв
        composable("mealList") {
            MealApp(navController, userName) // Передаємо контролер навігації та ім'я користувача
        }
        // Екран з деталями страви, який приймає idMeal як аргумент
        composable("mealDetail/{idMeal}") { backStackEntry ->
            val idMeal = backStackEntry.arguments?.getString("idMeal") ?: return@composable // Отримуємо idMeal з аргументів
            MealDetailScreen(idMeal) // Викликаємо екран деталізації страви
        }
    }
}
// Компонент для відображення екрану - головна сторінка з рекомендованими рецептами
@Composable
fun MealApp(navController: NavHostController, displayName: String?) {
    val mealViewModel: MealViewModel = viewModel() // Отримуємо ViewModel для роботи з даними страв
    // викликаємо метод для вибору 30 рандомних страв
    val meals = mealViewModel.mealsList
    val context = LocalContext.current // Отримуємо контекст для показу Toast
    // Показуємо Toast з привітанням, коли користувач входить в систему
    LaunchedEffect(displayName) {
        Toast.makeText(context, "Вітаємо, ${displayName ?: "Гість"}!", Toast.LENGTH_SHORT).show()
    }
    // Показуємо індикатор завантаження, поки список порожній
    if (meals.isEmpty()) {
        // Запускаємо завантаження даних один раз, якщо список порожній
        LaunchedEffect(Unit) {
            mealViewModel.fetchRandomMeals()
        }
        // Індикатор завантаження
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Якщо страви завантажені, показуємо UI
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "Recommended Recipes",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp)
            )
            // Створюємо сітку для відображення страв
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(8.dp),
                contentPadding = PaddingValues(4.dp)
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


