package com.example.cookup.data.repository

import android.net.Uri
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import retrofit2.await
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.util.UUID

// Репозиторій для роботи з Api та firebase
class MealRepository {
    var recommendedMeals = mutableStateListOf<Meal>()
        private set
    var favoriteMeals = mutableStateListOf<Meal>()
    var favoriteIds = mutableStateListOf<String>()
    private val database = FirebaseDatabase.getInstance()
    private val storageReference = FirebaseStorage.getInstance().reference
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    // Метод для отримання випадкових страв - використовуємо асинхронні запити
    suspend fun fetchRandomMeals() {
        coroutineScope {
            try {
                // Виконуємо 20 запитів паралельно
                val deferredMeals = (1..16).map {
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
                // Перевіряємо страви на унікальність
                val uniqueMeals = fetchedMeals.filter { meal ->
                    recommendedMeals.none { it.idMeal == meal.idMeal }
                }
                // Оновлюємо список страв
                recommendedMeals.addAll(uniqueMeals)
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

    // Метод для синхронізації з favorite
    suspend fun syncWithFavorites(meals: List<Meal>, ids:List<String>) {
        getFavoriteRecipes()
        meals.forEach { meal ->
            meal.isFavorite = ids.contains(meal.idMeal)
            Log.d("MealSync", "Meal ID: ${meal.idMeal}, isFavorite: ${meal.isFavorite}")
        }
    }

    // Метод для додавання авторського рецепту до Firebase
    suspend fun addMealToFirebase(meal: Meal) {
        val mealRef = userId?.let { database.reference.child("new_recipes").child("users").child(it).child(meal.idMeal) }
        try {
            mealRef?.setValue(meal.toMap())?.await()
        } catch (e: Exception) {
            Log.e("MealRepository","Failed to add meal: ${e.message}")
        }
    }

    // Метод для отримання всіх авторських рецептів користувача з Firebase
    suspend fun getMealsFromFirebase(): List<Meal> {
        val mealsRef = userId?.let { database.reference.child("new_recipes").child("users").child(it) }
        return try {
            val snapshot = mealsRef?.get()?.await()
            val meals = mutableListOf<Meal>()
            if (snapshot != null) {
                snapshot.children.forEach { dataSnapshot ->
                    val meal = dataSnapshot.getValue<Meal>()
                    if (meal != null) meals.add(meal)
                }
            }
            Log.d("MealRepository","Meals fetched ")
            meals
        } catch (e: Exception) {
            Log.e("MealRepository","Failed to retrieve meals: ${e.message}")
            throw Exception("Failed to retrieve meals: ${e.message}")
        }
    }


    fun uploadImageToFirebase(imageUri: Uri, onResult: (String?) -> Unit) {
        val fileName = "images/${UUID.randomUUID()}.jpg"
        val imageRef = storageReference.child(fileName)

        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    onResult(uri.toString()) // URL завантаженого зображення
                }
            }
            .addOnFailureListener {
                onResult(null) // У разі помилки
            }
    }

    suspend fun getAllMealsFromFirebase(): List<Meal> {
        val mealsRef = database.reference.child("new_recipes").child("users")

        return try {
            val snapshot = mealsRef.get().await()
            val meals = mutableListOf<Meal>()
            if (snapshot.exists()) {
                snapshot.children.forEach { userSnapshot ->
                    userSnapshot.children.forEach { mealSnapshot ->
                        val meal = mealSnapshot.getValue<Meal>()
                        if (meal != null) meals.add(meal)
                    }
                }
            }
            Log.d("MealRepository", "Meals fetched from all users")
            meals
        } catch (e: Exception) {
            Log.e("MealRepository", "Failed to retrieve meals: ${e.message}")
            throw Exception("Failed to retrieve meals: ${e.message}")
        }
    }

    // Метод для спостереження за змінами, якщо хтось додав нову страву
    fun observeMeals(callback: (List<Meal>) -> Unit) {
        val mealsRef = database.reference.child("new_recipes").child("users")
        mealsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val meals = mutableListOf<Meal>()
                if (snapshot.exists()) {
                    snapshot.children.forEach { userSnapshot ->
                        userSnapshot.children.forEach { mealSnapshot ->
                            val meal = mealSnapshot.getValue<Meal>()
                            if (meal != null) meals.add(meal)
                        }
                    }
                }
                callback(meals)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MealRepository", "Failed to observe meals: ${error.message}")
            }
        })
    }

}
