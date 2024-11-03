package com.example.cookup.models

// Модель відповіді для отримання даних про страви з API
data class MealResponse(
    // Список страв, що містить інформацію про кожну страву
    val meals: List<Meal>? = null
)

// Варто додати новий data class UIMeal зі списками strIngredients та strMeasures
// Перетворювати Meal в UIMeal відразу після отримання даних і використовувати UIMeal для виведення даних
// Це все треба щоб не вкористовувати рефлексію mealData.javaClass.getDeclaredField("strIngredient$i")

// Модель, що представляє окрему страву
data class Meal(
    val idMeal:String,
    // Назва страви
    val strMeal: String,

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
    @JvmField val strIngredient1: String?,
    @JvmField val strIngredient2: String?,
    @JvmField val strIngredient3: String?,
    @JvmField val strIngredient4: String?,
    @JvmField val strIngredient5: String?,
    @JvmField val strIngredient6: String?,
    @JvmField val strIngredient7: String?,
    @JvmField val strIngredient8: String?,
    @JvmField val strIngredient9: String?,
    @JvmField val strIngredient10: String?,
    @JvmField val strIngredient11: String?,
    @JvmField val strIngredient12: String?,
    @JvmField val strIngredient13: String?,
    @JvmField val strIngredient14: String?,
    @JvmField val strIngredient15: String?,
    @JvmField val strIngredient16: String?,
    @JvmField val strIngredient17: String?,
    @JvmField val strIngredient18: String?,
    @JvmField val strIngredient19: String?,
    @JvmField val strIngredient20: String?,

    // Міри
    @JvmField val strMeasure1: String?,
    @JvmField val strMeasure2: String?,
    @JvmField val strMeasure3: String?,
    @JvmField val strMeasure4: String?,
    @JvmField val strMeasure5: String?,
    @JvmField val strMeasure6: String?,
    @JvmField val strMeasure7: String?,
    @JvmField val strMeasure8: String?,
    @JvmField val strMeasure9: String?,
    @JvmField val strMeasure10: String?,
    @JvmField val strMeasure11: String?,
    @JvmField val strMeasure12: String?,
    @JvmField val strMeasure13: String?,
    @JvmField val strMeasure14: String?,
    @JvmField val strMeasure15: String?,
    @JvmField val strMeasure16: String?,
    @JvmField val strMeasure17: String?,
    @JvmField val strMeasure18: String?,
    @JvmField val strMeasure19: String?,
    @JvmField val strMeasure20: String?

)