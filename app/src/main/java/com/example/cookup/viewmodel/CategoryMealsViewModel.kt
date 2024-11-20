package com.example.cookup.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookup.data.models.Meal
import com.example.cookup.data.repository.MealRepository
import kotlinx.coroutines.launch

// ViewModel для завантаження страв за категорією
class CategoryMealsViewModel : ViewModel() {
    private val mealRepository = MealRepository()

    // Список страв, який буде спостерігатися у Compose UI
    var mealsList = mutableStateListOf<Meal>()
        private set

    fun loadMealsByCategory(category: String, ids: List<String>) {
        viewModelScope.launch {
            val fetchedMeals = mealRepository.fetchMealsByCategory(category)
            mealRepository.syncWithFavorites(fetchedMeals, ids)
            mealsList.clear()
            mealsList.addAll(fetchedMeals)
        }
    }
}
