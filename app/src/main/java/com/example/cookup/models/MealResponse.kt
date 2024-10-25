package com.example.cookup.models

// Модель відповіді для отримання даних про страви з API
data class MealResponse(
    // Список страв, що містить інформацію про кожну страву
    val meals: List<Meal>?
)

// Модель, що представляє окрему страву
data class Meal(
    // Назва страви
    val strMeal: String,

    // URL-адреса зображення страви
    val strMealThumb: String,

    // Географічна область, до якої належить страва
    val strArea: String,

    // Категорія страви (наприклад, основна страва, десерт і т.д.)
    val strCategory: String
)