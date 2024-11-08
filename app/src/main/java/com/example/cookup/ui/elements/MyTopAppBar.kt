package com.example.cookup.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cookup.R
import com.example.cookup.viewmodel.CategoriesViewModel
import com.example.cookup.viewmodel.MealSearchViewModel



// Компонент для TopAppBar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(userName: String?, navController: NavHostController,) {
    var query by remember { mutableStateOf("") }
    var isSearchMode by remember { mutableStateOf(false) } // Відкриття/закриття пошукового поля
    val recommendedQueries = listOf("Chicken", "Soup", "Pasta", "Pizza", "Salad")
    TopAppBar(
        modifier = Modifier
            .height(60.dp)
            .border(0.1.dp, MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(0.dp)),
        title = {
            if (!isSearchMode) {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Hi ${userName ?: "User"}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = "What do you want to cook?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            } else {
                // Відображення пошукового поля
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = {
                        Text(
                            "Search for a meal",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondary)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.tertiary),
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { query = "" }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.clear),
                                    contentDescription = "Clear Icon",
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.secondary
                    )
                )
            }
        },
        actions = {
            IconButton(onClick = {
                isSearchMode = !isSearchMode
            }) { // Перемикання між пошуком і основним екраном
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )

    // Відображення рекомендованих запитів
    if (isSearchMode) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "Recommended queries",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onPrimary),
                modifier = Modifier.padding(top = 5.dp, start = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                items(recommendedQueries) { suggestion ->
                    TextButton(onClick = { query = suggestion }) {
                        Text(
                            text = suggestion,
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.background(MaterialTheme.colorScheme.tertiary)
                                .padding(4.dp)
                        )
                    }
                }
            }
            Button(
                onClick = {
                    navController.navigate("searchedMeals/$query")
                    isSearchMode = false},
                modifier = Modifier
                    .padding(top = 15.dp, bottom = 5.dp)
                    .align(Alignment.CenterHorizontally)
                    .height(35.dp)
                    .fillMaxWidth(0.4f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Text(
                    text = "Search",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary)
                )
            }

        }
    }
}

