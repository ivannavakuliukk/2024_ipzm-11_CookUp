package com.example.cookup.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookup.data.models.Meal
import kotlinx.coroutines.launch
import android.util.Log
import com.example.cookup.data.repository.MealRepository

// ViewModel для завантаження деталей конкретної страви
class MealDetailViewModel : ViewModel() {

    private val mealRepository = MealRepository()
    // Змінна для зберігання деталей страви
    var mealDetail = mutableStateOf<Meal?>(null)
        private set

    // Функція для отримання деталей страви
    fun fetchMealById(idMeal: String) {
        viewModelScope.launch {
            try {
                val mealData = mealRepository.fetchMealById(idMeal)
                if (mealData != null) {
                    Log.d("MealDetailViewModel", "Fetching meal with ID: $idMeal, ${mealData.isFavorite}")
                }
                mealData?.let { data ->
                    // Створюємо списки для зручності відображення в composable
                    val ingredients = mutableListOf<String>() // Список для інгредієнтів
                    val measures = mutableListOf<String>() // Список для мірок

                    // Додаємо інгредієнти та їх кількість до списків
                    for (i in 1..20) {
                        // Використовуємо рефлексію для отримання значень інгредієнтів та мірок
                        val ingredient = data.javaClass.getDeclaredField("strIngredient$i").get(mealData) as? String
                        val measure = data.javaClass.getDeclaredField("strMeasure$i").get(mealData) as? String
                        if (!ingredient.isNullOrEmpty()) ingredients.add(ingredient)
                        if (!measure.isNullOrEmpty()) measures.add(measure)
                    }
                    // Оновлюємо mealDetail з новими значеннями
                    mealDetail.value = mealData.copy(
                        ingredients = ingredients,
                        measures = measures,
                    )
                }
            } catch (e: Exception) {
                Log.e("MealDetailViewModel", "Error fetching meal details", e)
                mealDetail.value = null
            }
        }
    }
    fun isElementFavorite(idMeal: String, ids: List<String>):Boolean{
        return ids.contains(idMeal)
    }
}