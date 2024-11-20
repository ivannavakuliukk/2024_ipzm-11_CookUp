package com.example.cookup.data.repository

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.example.cookup.data.datasource.RetrofitInstance
import com.example.cookup.data.models.Area
import com.example.cookup.data.models.AreaResponse
import com.example.cookup.data.models.Category
import com.example.cookup.data.models.CategoryResponse
import com.example.cookup.data.models.Meal
import com.example.cookup.data.models.MealResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import retrofit2.await
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

// Репозиторій для роботи з Api та firebase
class MealRepository {
    var recommendedMeals = mutableStateListOf<Meal>()
        private set
    var favoriteMeals = mutableStateListOf<Meal>()
    var favoriteIds = mutableStateListOf<String>()
    private val database = FirebaseDatabase.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    // Метод для отримання випадкових страв - використовуємо асинхронні запити
    suspend fun fetchRandomMeals() {
        coroutineScope {
            try {
                // Виконуємо 20 запитів паралельно
                val deferredMeals = (1..30).map {
                    async {
                        try {
                            RetrofitInstance.api.getRandomMeal().meals?.firstOrNull()
                        } catch (e: Exception) {
                            Log.e("MealRepository", "Error fetching random meal in async", e)
                            null
                        }
                    }
                }
                val fetchedMeals = deferredMeals.awaitAll().filterNotNull()

                // Оновлюємо список страв
                recommendedMeals.clear()
                recommendedMeals.addAll(fetchedMeals)
            } catch (e: Exception) {
                Log.e("MealRepository", "Error fetching random meals", e)
            }
        }
    }

    // Функція для отримання страви за ID
    suspend fun fetchMealById(idMeal: String): Meal? {
        return try {
            val response: MealResponse = RetrofitInstance.api.getMealById(idMeal).await()
            val meal = response.meals?.firstOrNull()
            if (meal != null) {
                meal.isFavorite = favoriteIds.contains(meal.idMeal)
            }
            meal
        } catch (e: Exception) {
            Log.e("MealRepository", "Error fetching meal by ID", e)
            null
        }
    }

    // Функція для отримання категорій
    suspend fun fetchCategories(): List<Category> {
        return try {
            val response: CategoryResponse = RetrofitInstance.api.getCategories()
            response.categories
        } catch (e: Exception) {
            Log.e("MealRepository", "Error fetching categories", e)
            emptyList()
        }
    }

    // Функція для отримання областей
    suspend fun fetchAreas(): List<Area> {
        return try {
            val response: AreaResponse = RetrofitInstance.api.getAreas()
            response.meals
        } catch (e: Exception) {
            Log.e("MealRepository", "Error fetching areas", e)
            emptyList()
        }
    }

    // Функція для отримання страв за категорією
    suspend fun fetchMealsByCategory(category: String): List<Meal> {
        return try {
            val response: MealResponse = RetrofitInstance.api.getMealsByCategory(category)
            response.meals ?: emptyList()
        } catch (e: Exception) {
            Log.e("MealRepository", "Error fetching meals by category", e)
            emptyList()
        }
    }

    // Функція для отримання страв за областю
    suspend fun fetchMealsByArea(area: String): List<Meal> {
        return try {
            val response: MealResponse = RetrofitInstance.api.getMealsByArea(area)
            response.meals ?: emptyList()
        } catch (e: Exception) {
            Log.e("MealRepository", "Error fetching meals by area", e)
            emptyList()
        }
    }

    // Функція для пошуку страв за запитом
    suspend fun searchMeals(query: String): List<Meal> {
        return try {
            val response: MealResponse = RetrofitInstance.api.searchMeals(query)
            response.meals ?: emptyList()
        } catch (e: Exception) {
            Log.e("MealRepository", "Error searching meals", e)
            emptyList()
        }
    }

    // Додати рецепт до обраних
    suspend fun addRecipeToFavorites(idMeal: String) {
        userId?.let {
            try {
                val meal = fetchMealById(idMeal)
                if (meal != null) {
                    // Додаємо в Firebase
                    database.reference.child("favorites").child("users").child(userId).child(idMeal)
                        .setValue(true)
                        .addOnSuccessListener {
                            meal.isFavorite = true
                            favoriteMeals.add(meal)
                            favoriteIds.add(idMeal)
                            Log.e("MealRepository", "Recipe added: $idMeal, ${meal.isFavorite}")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("MealRepository", "Error adding recipe: $exception")
                        }
                } else {
                    Log.e("MealRepository", "Meal not found for id: $idMeal")
                }
            } catch (e: Exception) {
                Log.e("MealRepository", "Error removing recipe from favorites", e)
            }
        }
    }

    // Видалити рецепт з обраних
    suspend fun removeRecipeFromFavorites(idMeal: String) {
        userId?.let {
            try {
                val meal = fetchMealById(idMeal)
                if (meal != null) {
                    meal.isFavorite = false
                    // Видаляємо з Firebase
                    database.reference.child("favorites").child("users").child(userId).child(idMeal)
                        .removeValue()
                        .addOnSuccessListener {
                            meal.isFavorite = false
                            favoriteMeals.remove(meal)
                            favoriteIds.remove(idMeal)
                            Log.e("MealRepository", "Recipe deleted: $idMeal, ${meal.isFavorite}")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("MealRepository", "Error deleting recipe: $exception")
                        }
                } else {
                    Log.e("MealRepository", "Meal not found for id: $idMeal")
                }
            } catch (e: Exception) {
                Log.e("MealRepository", "Error removing recipe from favorites", e)
            }
        }
    }

    // Отримати обрані рецепти користувача - використовуємо асинхронні запити
    suspend fun getFavoriteRecipes() {
        userId?.let { id ->
            try {
                val snapshot = database.reference.child("favorites").child("users").child(id).get().await()
                favoriteIds.clear()
                favoriteIds.addAll(snapshot.children.mapNotNull { it.key })
                favoriteMeals.clear()
                coroutineScope {
                    val deferredMeals = favoriteIds.map { id ->
                        async {
                            fetchMealById(id)
                        }
                    }
                    val favorites = deferredMeals.awaitAll().filterNotNull()
                    favoriteMeals.addAll(favorites)
                }
            } catch (e: Exception) {
                Log.e("FavoritesRepository", "Error fetching favorite recipes", e)
            }
        }
    }


    suspend fun syncWithFavorites(meals: List<Meal>, ids:List<String>) {
        getFavoriteRecipes()
        meals.forEach { meal ->
            meal.isFavorite = ids.contains(meal.idMeal)
            Log.d("MealSync", "Meal ID: ${meal.idMeal}, isFavorite: ${meal.isFavorite}")
        }
    }

}
