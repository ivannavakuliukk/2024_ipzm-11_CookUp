package com.example.cookup

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cookup.models.Meal
import com.example.cookup.viewmodel.MealViewModel
import com.google.firebase.auth.FirebaseAuth
import coil.compose.rememberImagePainter
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.text.style.TextOverflow

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth // Змінна для роботи з Firebase Authentication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ініціалізація FirebaseAuth для роботи з обліковими записами
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser // Отримуємо інформацію про поточного користувача

        // Налаштування вмісту екрана
        setContent {
            MealApp(currentUser?.displayName) // Передаємо ім'я користувача в MealApp
        }
    }
}

// Компонент для відображення основного інтерфейсу сторінки
@Composable
fun MealApp(displayName: String?) {
    val mealViewModel: MealViewModel = viewModel() // Отримуємо ViewModel для роботи з даними страв
    val meals by remember { mutableStateOf(mealViewModel.mealsList) } // Спостережуваний список страв

    val context = LocalContext.current // Отримуємо контекст для показу Toast

    // Показуємо Toast з привітанням, коли користувач входить в систему
    LaunchedEffect(displayName) {
        Toast.makeText(context, "Вітаємо, ${displayName ?: "Гість"}!", Toast.LENGTH_SHORT).show()
    }

    Column(modifier = Modifier.padding(8.dp)) { // Додаємо Column для вертикального розташування елементів
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
                MealCard(meal) // Відображаємо картку з інформацією про страву
            }
        }
    }
}

// Компонент для відображення картки страви
@Composable
fun MealCard(meal: Meal) {
    Card(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(230.dp),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Завантажуємо зображення страви
            Image(
                painter = rememberImagePainter(meal.strMealThumb),
                contentDescription = meal.strMeal,
                modifier = Modifier
                    .size(128.dp)
                    .padding(bottom = 8.dp)
            )
            Text(
                text = meal.strMeal,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis // Додаємо три крапки, якщо текст не поміщається
            )
            Text(
                text = "Category: ${meal.strCategory}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Region: ${meal.strArea}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}