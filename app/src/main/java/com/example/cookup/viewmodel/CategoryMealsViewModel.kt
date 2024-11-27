package com.example.cookup.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    var isSynchronized by mutableStateOf(false)
        private set

    fun loadMealsByCategory(category: String, ids: List<String>) {
        viewModelScope.launch {
            val fetchedMeals = mealRepository.fetchMealsByCategory(category)
            mealRepository.syncWithFavorites(fetchedMeals, ids)
            mealsList.clear()
            mealsList.addAll(fetchedMeals)
        }
    }

    fun syncWithFavorites(ids: List<String>){
        isSynchronized = true
        viewModelScope.launch {
            mealRepository.syncWithFavorites(mealsList, ids)
            isSynchronized = false
        }
    }
}
