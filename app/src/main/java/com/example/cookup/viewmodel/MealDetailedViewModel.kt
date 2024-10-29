package com.example.cookup.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookup.models.Meal
import com.example.cookup.models.MealResponse
import com.example.cookup.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await
import android.util.Log

// ViewModel для завантаження деталей конкретної страви
class MealDetailViewModel : ViewModel() {
    // Змінна для зберігання деталей страви
    var mealDetail = mutableStateOf<Meal?>(null)
        private set

    // Функція для отримання даних страви за її ID
    fun fetchMealById(idMeal: String) {
        viewModelScope.launch { // Запускаємо корутину у контексті ViewModel
            try {
                Log.d("MealDetailViewModel", "Fetching meal with ID: $idMeal")
                val response: MealResponse = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getMealById(idMeal).await() // Асинхронний запит до API для отримання страви
                }

                // Якщо список страв не порожній, обробляємо перший елемент
                response.meals?.firstOrNull()?.let { mealData ->
                    // Створюємо списки для зручності відображення в composable
                    val ingredients = mutableListOf<String>() // Список для інгредієнтів
                    val measures = mutableListOf<String>() // Список для мірок

                    // Додаємо інгредієнти та їх кількість до списків
                    for (i in 1..20) {
                        // Використовуємо рефлексію для отримання значень інгредієнтів та мірок
                        val ingredient = mealData.javaClass.getDeclaredField("strIngredient$i").get(mealData) as? String
                        val measure = mealData.javaClass.getDeclaredField("strMeasure$i").get(mealData) as? String

                        // Якщо значення не порожнє, додаємо до відповідного списку
                        if (!ingredient.isNullOrEmpty()) ingredients.add(ingredient)
                        if (!measure.isNullOrEmpty()) measures.add(measure)
                    }

                    // Оновлюємо mealDetail з новими значеннями
                    mealDetail.value = mealData.copy(
                        ingredients = ingredients,
                        measures = measures
                    )
                    Log.d("MealDetailViewModel", "Ingredients: $ingredients")
                    Log.d("MealDetailViewModel", "Measures: $measures")
                }
            } catch (e: Exception) {
                Log.e("MealDetailViewModel", "Error fetching meal details", e)
                mealDetail.value = null // У випадку помилки обнуляємо mealDetail
            }
        }
    }
}