package com.example.cookup.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Об'єкт, що представляє інстанс Retrofit для налаштування API-запитів
object RetrofitInstance {
    // Ледачий ініціалізатор для API-сервісу, що використовується для виконання запитів
    val api: MealApiService by lazy {
        Retrofit.Builder() // Створюємо новий екземпляр Retrofit
            .baseUrl("https://www.themealdb.com/api/json/v1/1/") // Встановлюємо базову URL-адресу API
            .addConverterFactory(GsonConverterFactory.create()) // Додаємо конвертер для перетворення JSON у об'єкти Kotlin
            .build() // Будуємо Retrofit інстанс
            .create(MealApiService::class.java) // Створюємо реалізацію API-сервісу
    }
}