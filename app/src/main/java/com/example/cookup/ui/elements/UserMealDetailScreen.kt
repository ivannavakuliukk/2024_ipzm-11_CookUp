package com.example.cookup.ui.elements


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.cookup.R
import com.example.cookup.viewmodel.ProfileViewModel
import com.example.cookup.viewmodel.UserMealsViewModel

@Composable
fun UserMealDetailScreen(
    idMeal: String,
    navController: NavHostController,
    userMealsViewModel: UserMealsViewModel
) {
    val meal = userMealsViewModel.findMealById(idMeal)
    if(meal!= null) {

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(26.dp).padding(0.dp).padding(start = 0.dp)
                    )
                }
                Text(
                    text = meal.strMeal, // Назва страви
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Image(
                painter = rememberAsyncImagePainter(meal.strMealThumb),
                contentDescription = meal.strMeal,
                modifier = Modifier.fillMaxWidth().height(250.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Author: ${meal.author}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFFD5D69),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Ingredients:",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFFD5D69),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            // Відображення інгредієнтів і їх кількості
            meal.ingredients.zip(meal.measures).forEach { (ingredient, measure) ->
                if (ingredient.isNotEmpty() && measure.isNotEmpty()) {
                    Text(
                        text = "$ingredient - $measure",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Instructions:",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFFD5D69),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = meal.strInstructions,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}
