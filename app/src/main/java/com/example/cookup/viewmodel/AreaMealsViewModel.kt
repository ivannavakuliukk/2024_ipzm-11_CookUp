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

class AreaMealsViewModel : ViewModel() {
    // Список страв, який буде спостерігатися у Compose UI
    var mealsList = mutableStateListOf<Meal>()
        private set

    // Функція для отримання страв за категорією
    fun loadMealsByArea(area: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getMealsByArea(area)
                }
                val fetchedMeals = response.meals
                mealsList.clear()
                if (fetchedMeals != null) {
                    mealsList.addAll(fetchedMeals)
                }
            } catch (e: Exception) {
                Log.e("loadMealsByArea", "Error fetching meals by area", e)
            }
        }
    }
}