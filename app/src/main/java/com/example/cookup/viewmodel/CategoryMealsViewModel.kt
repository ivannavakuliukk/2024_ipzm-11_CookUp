package com.example.cookup.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookup.models.Meal
import com.example.cookup.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CategoryMealsViewModel : ViewModel() {
    // Список страв, який буде спостерігатися у Compose UI
    var mealsList = mutableStateListOf<Meal>()
        private set

    // Функція для отримання страв за категорією
    fun loadMealsByCategory(category: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getMealsByCategory(category)
                }
                val fetchedMeals = response.meals
                mealsList.clear()
                if (fetchedMeals != null) {
                    mealsList.addAll(fetchedMeals)
                }
            } catch (e: Exception) {
                Log.e("loadMealsByCategory", "Error fetching meals by category", e)
            }
        }
    }
}
