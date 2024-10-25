package com.example.cookup.network

import com.example.cookup.models.MealResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// Інтерфейс для визначення методів API, пов'язаних з отриманням інформації про страви
interface MealApiService {
    // Метод для отримання списку страв за першою літерою
    @GET("search.php") // Вказуємо ендпоінт для запиту
    fun searchMealsByLetter(@Query("f") firstLetter: String): Call<MealResponse>
}