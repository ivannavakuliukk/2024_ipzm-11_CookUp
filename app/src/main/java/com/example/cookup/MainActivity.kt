package com.example.cookup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.NavHostController
import com.example.cookup.ui.elements.MealDetailScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cookup.ui.elements.BottomNavigationBar
import com.example.cookup.ui.elements.CategoriesScreen
import com.example.cookup.ui.elements.FavoritesScreen
import com.example.cookup.ui.elements.HomeScreen
import com.example.cookup.ui.elements.MealsByAreaScreen
import com.example.cookup.ui.elements.MealsByCategoryScreen
import com.example.cookup.ui.elements.MyTopAppBar
import com.example.cookup.ui.elements.ProfileScreen
import com.example.cookup.ui.elements.SearchedMealsScreen
import com.example.cookup.ui.theme.CookUpTheme
import com.example.cookup.viewmodel.FavoritesViewModel


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
            CookUpTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) }
                ) { paddingValues ->
                    Column {
                        MyTopAppBar(userName = currentUser?.displayName, navController) // Додаємо TopAppBar
                        NavigationGraph(
                            navController = navController,
                            paddingValues = paddingValues
                        )
                    }
                }
            }
        }
    }

}

// Функція навігації
@Composable
fun NavigationGraph(navController: NavHostController, paddingValues: PaddingValues) {
    val favoritesViewModel: FavoritesViewModel = viewModel()
    // Визначаємо основні маршрути навігації
    NavHost(navController = navController, startDestination = "home", modifier = Modifier.padding(paddingValues)) {
        // Головний екран зі списком страв - початкова точка
        composable("home") {
            HomeScreen(navController, favoritesViewModel)
        }
        composable("categories") { CategoriesScreen(navController) }
        composable("favorites") { FavoritesScreen(navController, favoritesViewModel) }
        composable("profile") { ProfileScreen(navController, favoritesViewModel) }
        // Екран з деталями страви, який приймає idMeal як аргумент
        composable("mealDetail/{idMeal}") { backStackEntry ->
            val idMeal = backStackEntry.arguments?.getString("idMeal") ?: return@composable // Отримуємо idMeal з аргументів
            MealDetailScreen(idMeal, navController, favoritesViewModel) // Викликаємо екран деталізації страви
        }
        composable("mealsByCategory/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: return@composable
            MealsByCategoryScreen(category = category, navController, favoritesViewModel)
        }
        composable("mealsByArea/{area}") { backStackEntry ->
            val area = backStackEntry.arguments?.getString("area") ?: return@composable
            MealsByAreaScreen(area = area, navController, favoritesViewModel)
        }
        composable("searchedMeals/{query}") { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: return@composable
            SearchedMealsScreen(query = query, navController, favoritesViewModel)
        }
    }
}


