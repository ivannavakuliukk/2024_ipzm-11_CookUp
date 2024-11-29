package com.example.cookup.ui.elements


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cookup.R
import com.example.cookup.viewmodel.UserMealsViewModel

@Composable
fun UserMealsScreen(navController: NavHostController, userMealsViewModel: UserMealsViewModel) {
    val meals = userMealsViewModel.mealsList
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(8.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier.padding(0.dp).heightIn(max = 25.dp)
            ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(33.dp)
                                .padding(bottom = 3.dp, end = 8.dp)
                        )
                    }
                Text(
                    text = "Authors recipe",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

        }
        item(span = { GridItemSpan(2) }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(80.dp)
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    text = "Here you can see author`s recipe, added by app users. To add your recipe go to your profile page.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }

        }
        if(meals.isEmpty() && !userMealsViewModel.isLoading){
            item(span = { GridItemSpan(2) }) {
                Text(
                    "Nobody has`t added any recipes yet..",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }else if(userMealsViewModel.isLoading){
            item(span = { GridItemSpan(2) }) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
                }
            }
        } else {
            items(meals) { meal ->
                MealCard(
                    meal = meal,
                ) { idMeal ->
                    navController.navigate("UserMealDetail/$idMeal")
                }

            }
        }
    }
}

