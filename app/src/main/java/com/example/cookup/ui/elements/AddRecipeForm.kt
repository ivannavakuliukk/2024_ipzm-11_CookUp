package com.example.cookup.ui.elements

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.cookup.data.models.Meal
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cookup.viewmodel.ProfileViewModel
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeForm(onSubmit: (Meal) -> Unit, onCancel: () -> Unit, viewModel: ProfileViewModel, name: String) {
    var recipeName by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    val ingredients = remember { mutableStateListOf("", "", "", "", "") }
    val measures = remember { mutableStateListOf("", "", "", "", "") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {uri->
            selectedImageUri = uri
        }
    )
    var isUploading by remember { mutableStateOf(false) }

    // Перевірка полів
    val isRecipeNameValid = recipeName.isNotBlank() && recipeName.length >= 3
    val isInstructionsValid = instructions.isNotBlank() && instructions.length >= 30
    val areIngredientsValid = ingredients.all { it.isNotBlank() }
    val areMeasuresValid = measures.all { it.isNotBlank() }
    val isFormValid = isRecipeNameValid && isInstructionsValid && areIngredientsValid && areMeasuresValid
    var errorMessage by remember { mutableStateOf("") }

    // Кольори, щоб уникнути повторів
    val textFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = MaterialTheme.colorScheme.tertiary.copy(0.3f),
        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
        cursorColor = MaterialTheme.colorScheme.primary
    )
    val buttonColors =  ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.tertiary.copy(0.1f),
        contentColor = MaterialTheme.colorScheme.onPrimary
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (isUploading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 0.dp)
            ) {
                Text(
                    text = "Add a new recipe",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                // Виведення повідомлення про помилку
                if (errorMessage.isNotBlank()) {
                    Text(
                        text = "!! {$errorMessage} !!",
                        color = Color.Yellow,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
                // Кнопки для відміни та публікування
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f).height(35.dp),
                        colors = buttonColors,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                    ) {
                        Text("Cancel", style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.width(15.dp))
                    Button(
                        onClick = {
                            if (isFormValid && selectedImageUri != null) {
                                isUploading = true
                                viewModel.uploadImage(selectedImageUri!!) { uploadedImageUrL ->
                                    if (uploadedImageUrL != null) {
                                        val meal = Meal(
                                            idMeal = UUID.randomUUID().toString(),
                                            author = name,
                                            strMeal = recipeName,
                                            strMealThumb = uploadedImageUrL,
                                            strArea = "",
                                            strCategory = "",
                                            strInstructions = instructions,
                                            ingredients = ingredients.filter { it.isNotBlank() },
                                            measures = measures.filter { it.isNotBlank() }
                                        )
                                        viewModel.addMeal(meal)
                                        onSubmit(meal)
                                        Log.d("AddRecipeForm", meal.author)
                                        isUploading = false
                                    } else {
                                        isUploading = false
                                        errorMessage =
                                            "Please fill in all required fields correctly."
                                    }
                                }
                            } else {
                                errorMessage = "Please fill in all fields and upload image."
                            }
                        },
                        colors = buttonColors,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.weight(1f).height(35.dp),
                        enabled = isFormValid && !isUploading && selectedImageUri != null
                    ) {
                        Text(
                            "Publish Recipe",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                // Поле для вибору зображення
                if (selectedImageUri == null) {
                    Button(
                        colors = buttonColors,
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth().height(35.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                    ) {
                        Text("Select image", style = MaterialTheme.typography.bodySmall)
                    }
                } else {
                    Text("Image selected", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Selected Recipe Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                // Заголовки та поля вводу
                Text(
                    text = "Recipe Name",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                TextField(
                    value = recipeName,
                    onValueChange = { recipeName = it },
                    placeholder = {
                        Text(
                            "Recipe Name",
                            style = MaterialTheme.typography.displaySmall
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    textStyle = MaterialTheme.typography.displaySmall,
                    colors = textFieldColors,
                    singleLine = true
                )
                Text(
                    text = "Recipe Instruction",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                TextField(
                    value = instructions,
                    onValueChange = { instructions = it },
                    placeholder = {
                        Text(
                            "Instructions",
                            style = MaterialTheme.typography.displaySmall
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    textStyle = MaterialTheme.typography.displaySmall,
                    colors = textFieldColors
                )
                Text(
                    text = "Ingredients",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                // Поля для інгредієнтів та мір
                for (i in ingredients.indices) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = ingredients[i],
                            onValueChange = { ingredients[i] = it },
                            placeholder = {
                                Text(
                                    "Ingredient ${i + 1}",
                                    style = MaterialTheme.typography.displaySmall
                                )
                            },
                            modifier = Modifier
                                .weight(0.75f)
                                .height(48.dp),
                            textStyle = MaterialTheme.typography.displaySmall,
                            colors = textFieldColors
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TextField(
                            value = measures[i],
                            onValueChange = { measures[i] = it },
                            placeholder = {
                                Text(
                                    "Amt",
                                    style = MaterialTheme.typography.displaySmall
                                )
                            },
                            modifier = Modifier
                                .weight(0.25f)
                                .height(48.dp),
                            textStyle = MaterialTheme.typography.displaySmall,
                            colors = textFieldColors
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Кнопки для додавання та видалення нових інгредієнтів
                Button(
                    onClick = {
                        if (ingredients.size < 21) {
                            ingredients.add("")
                            measures.add("")
                        }
                    },
                    enabled = ingredients.size < 21,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("+ Add Ingredient", style = MaterialTheme.typography.bodySmall)
                }
                Button(
                    onClick = {
                        if (ingredients.size > 2) {
                            ingredients.removeAt(ingredients.size - 1)
                            measures.removeAt(measures.size - 1)
                        }
                    },
                    enabled = ingredients.size > 2,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                ) {
                    Text("- Delete Ingredient", style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}