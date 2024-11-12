package com.example.cookup.data.models

// Модель відповіді для отримання даних про категорії з API
data class CategoryResponse(
    val categories: List<Category>
)

// Модель, що представляє окрему категорію
data class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String
)