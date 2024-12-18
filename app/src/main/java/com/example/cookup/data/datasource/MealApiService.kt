package com.example.cookup.data.datasource

import com.example.cookup.data.models.AreaResponse
import com.example.cookup.data.models.CategoryResponse
import com.example.cookup.data.models.MealResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// Інтерфейс для визначення методів API, пов'язаних з отриманням інформації про страви
interface MealApiService {
    // Метод для отримання страви за ID
    @GET("lookup.php")
    fun getMealById(@Query("i") idMeal: String): Call<MealResponse>
    // Метод для отримання випадкової страви
    @GET("random.php")
    suspend fun getRandomMeal(): MealResponse
    // Метод для отримання категорій
    @GET("categories.php")
    suspend fun getCategories(): CategoryResponse
    // Метод для отримання областей
    @GET("list.php?a=list")
    suspend fun getAreas(): AreaResponse
    // Метод для отримання страв за категорією
    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") category: String): MealResponse
    // Метод для отримання страв за областю
    @GET("filter.php")
    suspend fun getMealsByArea(@Query("a") category: String): MealResponse
    // Метод для отримання страв за пошуковим запитом
    @GET("search.php")
    suspend fun searchMeals(@Query("s") query: String): MealResponse
}