package com.example.cookup.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookup.models.Meal
import com.example.cookup.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MealSearchViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)// Стан завантаження
        private set
    var mealsList = mutableStateListOf<Meal>()
        private set

    // Функція для отримання страв за пошуковим запитом
    fun searchMeals(query: String) {
        if (query.length < 3) return
        isLoading = true
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.searchMeals(query)
                }
                val fetchedMeals = response.meals
                mealsList.clear()
                if (fetchedMeals != null) {
                    mealsList.addAll(fetchedMeals)
                }
            } catch (e: Exception) {
                Log.e("searchMeals", "Error searching meals", e)
            }finally {
                isLoading = false
            }
        }
    }
}