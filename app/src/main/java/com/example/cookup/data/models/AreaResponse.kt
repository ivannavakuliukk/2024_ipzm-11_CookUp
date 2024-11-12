package com.example.cookup.data.models

// Модель відповіді для отримання даних про області з API
data class AreaResponse(
    val meals: List<Area>
)

// Модель, що представляє окрему область
data class Area(
    val strArea: String
)