package com.example.cookup.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.cookup.models.Meal

// Компонент для відображення картки страви
@Composable
fun MealCard(meal: Meal, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(230.dp)
            .clickable { onClick(meal.idMeal) },
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