package com.example.cookup.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookup.data.models.Meal
import com.example.cookup.data.repository.MealRepository
import kotlinx.coroutines.launch

// ViewModel для завантаження страв за областю
class AreaMealsViewModel : ViewModel() {
    private val mealRepository = MealRepository()

    // Список страв, який буде спостерігатися у Compose UI
    var mealsList = mutableStateListOf<Meal>()
        private set

    // Функція для отримання страв за областю
    fun loadMealsByArea(area: String, ids: List<String>) {
        viewModelScope.launch {
            val fetchedMeals = mealRepository.fetchMealsByArea(area)
            mealRepository.syncWithFavorites(fetchedMeals, ids)
            mealsList.clear()
            mealsList.addAll(fetchedMeals)
        }
    }
}