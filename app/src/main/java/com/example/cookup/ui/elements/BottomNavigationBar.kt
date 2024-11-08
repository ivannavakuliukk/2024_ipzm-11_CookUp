package com.example.cookup.ui.elements

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cookup.R

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

    NavigationBar(modifier = Modifier.height(50.dp), containerColor = MaterialTheme.colorScheme.tertiary) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item -> // Проходимо через всі пункти навігації
            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                label = null,
                selected = currentRoute == item.route, // Визначаємо, чи пункт вибраний
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondary
                ),
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