package com.example.cookup.viewmodel

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
import retrofit2.await

// ViewModel для роботи зі стравами, що отримуються з API
class MealViewModel : ViewModel() {
    // Список страв, який буде спостерігатися у Compose UI
    var mealsList = mutableStateListOf<Meal>()
        private set

    // Ініціалізація ViewModel, де запускається завантаження всіх страв
    init {
        //fetchAllMeals()
        fetchAllMeals()
    }

    // Функція для отримання страв за першою літерою
    private fun fetchMealsByLetter(letter: String) {
        viewModelScope.launch { // Запускаємо нову корутину в контексті ViewModel
            try {
                // Отримання відповіді від API в фоновому потоці
                val response: MealResponse = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.searchMealsByLetter(letter).await()
                }

                // Додаємо одну випадкову страву в mealsList, якщо вони є в відповіді
                response.meals?.let { meals ->
                    if (meals.isNotEmpty()) {
                        val randomMeal = meals.random() // Вибираємо випадкову страву
                        mealsList.add(randomMeal) // Додаємо її в mealsList
                    }
                }
            } catch (e: Exception) {
                // Обробка помилок, якщо запит не вдався
            }
        }
    }

    // Функція для отримання всіх страв - 30 рандомних
    private fun fetchAllMeals() {
        viewModelScope.launch {
            // Створюємо список корутин для кожної літери
            val deferredMeals = ('a'..'z').map { letter ->
                async { fetchMealsByLetter(letter.toString()) } // Викликаємо fetchMealsByLetter паралельно
            }

            // Чекаємо завершення всіх запитів
            deferredMeals.awaitAll()

            // Перемішуємо список страв кілька разів
            repeat(5) { mealsList.shuffle() }
        }
    }

}
