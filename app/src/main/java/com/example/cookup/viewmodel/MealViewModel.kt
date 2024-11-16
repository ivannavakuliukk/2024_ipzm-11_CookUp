package com.example.cookup.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookup.data.models.Meal
import com.example.cookup.data.repository.MealRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

// ViewModel для завантаження рекомендованих страв
class MealViewModel : ViewModel() {
    private val mealRepository = MealRepository()

    // Список страв, який буде спостерігатися у Compose UI
    var mealsList = mutableStateListOf<Meal>()
        private set

    init {
        fetchRandomMeals()
    }

    // Функція для отримання 30 випадкових страв
    fun fetchRandomMeals() {
        viewModelScope.launch {
            val deferredMeals = (1..30).map {
                async { mealRepository.fetchRandomMeal() }
            }
            val meals = deferredMeals.awaitAll()
            Log.d("MealViewModel", "Meals fetched: ${meals.size}")
            mealsList.addAll(meals.filterNotNull())
            Log.d("MealViewModel", "Meals fetched: ${mealsList.size}")
        }
    }
}

