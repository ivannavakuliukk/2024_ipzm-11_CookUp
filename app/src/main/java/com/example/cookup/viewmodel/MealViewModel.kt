package com.example.cookup.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookup.models.Meal
import com.example.cookup.models.MealResponse
import com.example.cookup.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// ViewModel для роботи зі стравами, що отримуються з API
class MealViewModel : ViewModel() {
    // Список страв, який буде спостерігатися у Compose UI
    var mealsList = mutableStateListOf<Meal>()
        private set

    // Функція для отримання 30 випадкових страв
    fun fetchRandomMeals() {
        viewModelScope.launch {
            val deferredMeals = (1..30).map { // Створюємо список корутин для 30 запитів
                async { fetchRandomMeal() }
            }
            // Чекаємо завершення всіх запитів
            val meals = deferredMeals.awaitAll()
            Log.d("MealViewModel", "Meals fetched: ${meals.size}")
            // Додаємо отримані страви до списку
            mealsList.addAll(meals.filterNotNull())
            Log.d("MealViewModel", "Meals fetched: ${mealsList.size}")
        }
    }

    // Функція для отримання випадкової страви
    private suspend fun fetchRandomMeal(): Meal? {
        return try {
            // Отримання відповіді від API в фоновому потоці
            withContext(Dispatchers.IO) {
                val response: MealResponse = RetrofitInstance.api.getRandomMeal()
                response.meals?.firstOrNull() // Повертаємо першу страву, якщо вона є
            }
        } catch (e: Exception) {
            Log.e("fetchRandomMeal", "Error fetching random meal", e)
            null
        }
    }
    // Функція для отримання страв за першою літерою
//    private fun fetchMealsByLetter(letter: String) {
//        viewModelScope.launch { // Запускаємо нову корутину в контексті ViewModel
//            try {
//                // Отримання відповіді від API в фоновому потоці
//                val response: MealResponse = withContext(Dispatchers.IO) {
//                    RetrofitInstance.api.searchMealsByLetter(letter).await()
//                }
//
//                // Додаємо всі страви за цією літерою
//                response.meals?.let { meals ->
//                    if (meals.isNotEmpty()) {
//                        mealsList.add(meals)
//                    }
//                }
//            } catch (e: Exception) {
//                // Обробка помилок, якщо запит не вдався
//            }
//        }
//    }
}
