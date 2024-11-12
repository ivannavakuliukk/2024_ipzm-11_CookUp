package com.example.cookup.ui.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cookup.data.models.Category
import com.example.cookup.data.models.Area
import com.example.cookup.viewmodel.CategoriesViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter

// Екран з відображенням категорій і областей
@Composable
fun CategoriesScreen( navController: NavHostController) {
    val viewModel: CategoriesViewModel = viewModel()
    val categories = viewModel.categories
    val areas = viewModel.meals
    if (categories.isEmpty() && areas.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            // Заголовок для категорій
            item(span = { GridItemSpan(2) }) {
                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(8.dp),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            // Відображення категорій
            items(categories) { category ->
                CategoryCard(category) {category->
                    navController.navigate("mealsByCategory/$category")
                }
            }

            // Заголовок для областей
            item(span = { GridItemSpan(2) }) {
                Text(
                    text = "Areas",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(8.dp),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            // Відображення областей
            items(areas) { area ->
                AreaCard(area) {area->
                    navController.navigate("mealsByArea/$area")
                }
            }
        }
    }
}

// Компонент для картки категорій
@Composable
fun CategoryCard(category: Category, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(170.dp)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.secondary))
            .fillMaxWidth()
            .clickable { onClick(category.strCategory) },
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(category.strCategoryThumb),
                contentDescription = category.strCategory,
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.strCategory,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

// Компонент для картки областей
@Composable
fun AreaCard(area: Area, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.secondary))
            .fillMaxWidth()
            .clickable { onClick(area.strArea) },
        elevation = CardDefaults.elevatedCardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = area.strArea,
            style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
