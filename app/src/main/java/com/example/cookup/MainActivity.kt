package com.example.cookup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.cookup.ui.elements.MealDetailScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cookup.ui.elements.CategoriesScreen
import com.example.cookup.ui.elements.FavoritesScreen
import com.example.cookup.ui.elements.HomeScreen
import com.example.cookup.ui.elements.ProfileScreen


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
            val navController = rememberNavController()
            Scaffold(
                bottomBar = { BottomNavigationBar(navController) }
            ) {paddingValues ->
                Column {
                    MyTopAppBar(userName = currentUser?.displayName) // Додаємо TopAppBar
                    NavigationGraph(
                        navController = navController,
                        userName = currentUser?.displayName,
                        paddingValues = paddingValues
                    )
                }
            }
        }
    }
}

// Функція навігації
@Composable
fun NavigationGraph(navController: NavHostController, userName: String?, paddingValues: PaddingValues) {
    // Визначаємо основні маршрути навігації
    NavHost(navController = navController, startDestination = "home", modifier = Modifier.padding(paddingValues)) {
        // Головний екран зі списком страв - початкова точка
        composable("home") {
            HomeScreen(navController, userName)
        }
        composable("categories") { CategoriesScreen() }
        composable("favorites") { FavoritesScreen() }
        composable("profile") { ProfileScreen() }
        // Екран з деталями страви, який приймає idMeal як аргумент
        composable("mealDetail/{idMeal}") { backStackEntry ->
            val idMeal = backStackEntry.arguments?.getString("idMeal") ?: return@composable // Отримуємо idMeal з аргументів
            MealDetailScreen(idMeal) // Викликаємо екран деталізації страви
        }
    }
}

// Клас для зберігання даних про пункт навігації
data class BottomNavItem(
    val route: String,// маршрут для навігації
    val icon: Int // ресурс іконки
)

// Визначення пунктів навігації
val bottomNavItems = listOf(
    BottomNavItem(route = "home", icon = R.drawable.home),
    BottomNavItem(route = "categories", icon = R.drawable.category),
    BottomNavItem(route = "favorites", icon = R.drawable.heart),
    BottomNavItem(route = "profile", icon = R.drawable.user)
)

// Компонент для нижньої панелі навігації
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = bottomNavItems // отримуємо список пунктів навігації

    NavigationBar(modifier = Modifier.height(50.dp), containerColor = Color(0xFFD0BCFF)) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item -> // Проходимо через всі пункти навігації
            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = null,
                selected = currentRoute == item.route, // Визначаємо, чи пункт вибраний
                onClick = {
                    navController.navigate(item.route) { // Навігація до вибраного маршруту
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

// Компонент для TopAppBar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(userName: String?) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Hi ${userName ?: "User"}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "What do you want to cook?",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Тут буде логіка для відкриття поля вводу */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFD0BCFF)
        )
    )
}
