package com.example.cookup.data.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

// Модель відповіді для отримання даних про страви з API
data class MealResponse(
    // Список страв, що містить інформацію про кожну страву
    val meals: List<Meal>? = null
)

// Модель, що представляє окрему страву
data class Meal(
    val idMeal:String,
    // Назва страви
    val strMeal: String,
    var author: String = "",

    // URL-адреса зображення страви
    val strMealThumb: String,

    // Географічна область, до якої належить страва
    val strArea: String,

    // Категорія страви (наприклад, основна страва, десерт і т.д.)
    val strCategory: String,

    // Інструкція з приготування
    val strInstructions: String,

    /*
    Примітка: модель містить багато полів через специфікацію API,
    яке повертає інгредієнти та їх міри окремо (strIngredient1, strMeasure1 тощо)
    Це призводить до великої кількості полів у моделі
     */
    // Інгредієнти та їх кількість (списки для зручності у відображенні в composable)
    val ingredients: List<String> = emptyList(),
    val measures: List<String> = emptyList(),
    // Інгрідієнти
    @JvmField val strIngredient1: String? = null,
    @JvmField val strIngredient2: String? = null,
    @JvmField val strIngredient3: String? = null,
    @JvmField val strIngredient4: String? = null,
    @JvmField val strIngredient5: String? = null,
    @JvmField val strIngredient6: String? = null,
    @JvmField val strIngredient7: String? = null,
    @JvmField val strIngredient8: String? = null,
    @JvmField val strIngredient9: String? = null,
    @JvmField val strIngredient10: String? = null,
    @JvmField val strIngredient11: String? = null,
    @JvmField val strIngredient12: String? = null,
    @JvmField val strIngredient13: String? = null,
    @JvmField val strIngredient14: String? = null,
    @JvmField val strIngredient15: String? = null,
    @JvmField val strIngredient16: String? = null,
    @JvmField val strIngredient17: String? = null,
    @JvmField val strIngredient18: String? = null,
    @JvmField val strIngredient19: String? = null,
    @JvmField val strIngredient20: String? = null,

    // Міри
    @JvmField val strMeasure1: String? = null,
    @JvmField val strMeasure2: String? = null,
    @JvmField val strMeasure3: String? = null,
    @JvmField val strMeasure4: String? = null,
    @JvmField val strMeasure5: String? = null,
    @JvmField val strMeasure6: String? = null,
    @JvmField val strMeasure7: String? = null,
    @JvmField val strMeasure8: String? = null,
    @JvmField val strMeasure9: String? = null,
    @JvmField val strMeasure10: String? = null,
    @JvmField val strMeasure11: String? = null,
    @JvmField val strMeasure12: String? = null,
    @JvmField val strMeasure13: String? = null,
    @JvmField val strMeasure14: String? = null,
    @JvmField val strMeasure15: String? = null,
    @JvmField val strMeasure16: String? = null,
    @JvmField val strMeasure17: String? = null,
    @JvmField val strMeasure18: String? = null,
    @JvmField val strMeasure19: String? = null,
    @JvmField val strMeasure20: String? = null,

){
    var isFavorite = false
    // Функція для перетворення Meal в Map
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "idMeal" to idMeal,
            "strMeal" to strMeal,
            "strMealThumb" to strMealThumb,
            "strArea" to strArea,
            "strCategory" to strCategory,
            "strInstructions" to strInstructions,
            "ingredients" to ingredients,
            "measures" to measures,
            "author" to author
        )
    }
    constructor() : this("","","", "", "", "", "", listOf(), listOf())
}