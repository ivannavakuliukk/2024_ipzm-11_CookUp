package com.example.cookup.data.repository

import android.util.Log
import com.example.cookup.data.datasource.RetrofitInstance
import com.example.cookup.data.models.Area
import com.example.cookup.data.models.AreaResponse
import com.example.cookup.data.models.Category
import com.example.cookup.data.models.CategoryResponse
import com.example.cookup.data.models.Meal
import com.example.cookup.data.models.MealResponse
import retrofit2.await

// Репозиторій для роботи з Api
class MealRepository {

    // Функція для отримання випадкової страви
    suspend fun fetchRandomMeal(): Meal? {
        return try {
            val response: MealResponse = RetrofitInstance.api.getRandomMeal()
            response.meals?.firstOrNull() // Повертаємо першу страву, якщо вона є
        } catch (e: Exception) {
            Log.e("MealRepository", "Error fetching random meal", e)
            null
        }
    }

    // Функція для отримання страви за ID
    suspend fun fetchMealById(idMeal: String): Meal? {
        return try {
            val response: MealResponse = RetrofitInstance.api.getMealById(idMeal).await()
            response.meals?.firstOrNull()
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
}
