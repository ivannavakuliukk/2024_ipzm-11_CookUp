package com.example.cookup.ui.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.cookup.R
import com.example.cookup.data.models.Meal

// Компонент для відображення картки страви
@Composable
fun MealFavoriteCard(meal: Meal, isFavorite: State<Boolean>, onFavoriteClick: () -> Unit, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(205.dp)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.secondary))
            .clickable { onClick(meal.idMeal) },
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RectangleShape
    ) {
        Box(
            modifier = Modifier.padding(0.dp)
        ) {
            // Завантажуємо зображення страви
            Image(
                painter = rememberAsyncImagePainter(meal.strMealThumb),
                contentDescription = meal.strMeal,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(160.dp)
                    .align(Alignment.TopCenter)
            )

            // Серце для додавання/видалення з обраних
            IconButton(onClick = onFavoriteClick, modifier = Modifier
                .align(Alignment.TopEnd)) {
                Icon(
                    painter = painterResource(id = R.drawable.heart_filled),
                    contentDescription = "Favorite Icon",
                    tint = if (isFavorite.value) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(horizontal = 2.dp, vertical = 10.dp).size(20.dp)
                )
            }
        }
        Column(
            modifier = Modifier.padding(horizontal = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = meal.strMeal,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
