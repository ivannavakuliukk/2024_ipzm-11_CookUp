package com.example.cookup.ui.elements


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.cookup.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(navController: NavHostController, name: String, photoUrl: String) {
    val profileViewModel: ProfileViewModel = viewModel()
    val meals = profileViewModel.mealsList
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var showForm by remember { mutableStateOf(false) }

    val buttonColors =  ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.tertiary.copy(0.1f),
        contentColor = MaterialTheme.colorScheme.onPrimary
    )

    // Виклик getMeals після додавання страви
    LaunchedEffect(profileViewModel.isMealAdded) {
        if (profileViewModel.isMealAdded) {
            profileViewModel.getMeals()
        }
    }

    if (!showForm) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(8.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item(span = { GridItemSpan(2) }) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (photoUrl.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(photoUrl),
                                contentDescription = "User Profile Picture",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = name,
                            style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                item(span = { GridItemSpan(2) }) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Button(
                            onClick = { showForm = true },
                            modifier = Modifier.weight(1f).height(35.dp),
                            colors = buttonColors,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
                        ) {
                            Text("Add Your Recipe", style = MaterialTheme.typography.displaySmall)
                        }
                        Spacer(modifier = Modifier.width(15.dp))
                        Button(
                            onClick = { showDialog = true  },
                            modifier = Modifier.weight(1f).height(35.dp),
                            colors = buttonColors,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
                        ) {
                            Text("Logout", style = MaterialTheme.typography.displaySmall)
                        }
                        if (showDialog) {
                            LogoutConfirmationDialog(
                                onConfirm = {
                                    profileViewModel.logout(context)
                                    showDialog = false
                                },
                                onDismiss = { showDialog = false }
                            )
                        }
                    }
                }
                if(meals.isEmpty() && !profileViewModel.isLoading){
                    item(span = { GridItemSpan(2) }) {
                        Text(
                            "You haven`t added any recipes yet..",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }else if(profileViewModel.isLoading){
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
        } else {
            // Форма додавання рецепта
            AddRecipeForm(
                onSubmit = { meal ->
                    println("Recipe submitted: $meal")
                    showForm = false
                },
                onCancel = { showForm = false },
                profileViewModel,
                name
            )
        }
}


@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val buttonColors =  ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.tertiary.copy(0.1f),
        contentColor = MaterialTheme.colorScheme.onPrimary
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Exit confirmation", style = MaterialTheme.typography.bodyMedium) },
        text = { Text(text = "Are you sure you want to exit?", style = MaterialTheme.typography.bodySmall) },
        confirmButton = {
            TextButton(onClick = onConfirm, colors = buttonColors) {
                Text("Yes", style = MaterialTheme.typography.bodySmall)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, colors = buttonColors) {
                Text("Cancel", style = MaterialTheme.typography.bodySmall)
            }
        }
    )
}
