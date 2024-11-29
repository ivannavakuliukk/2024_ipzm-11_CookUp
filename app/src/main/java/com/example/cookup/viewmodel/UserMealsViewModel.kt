package com.example.cookup.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookup.data.models.Meal
import com.example.cookup.data.repository.MealRepository
import kotlinx.coroutines.launch

class UserMealsViewModel: ViewModel() {
    private val mealRepository = MealRepository()
    val mealsList = mutableStateListOf<Meal>()
    var isLoading by mutableStateOf(false)
    init {
        observeMeals()
    }

    // Метод для отримання всіх рецептів
    fun getMeals() {
        isLoading = true
        viewModelScope.launch {
            try {
                mealsList.clear()
                mealsList.addAll(mealRepository.getAllMealsFromFirebase())
                isLoading = false
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error getting meals")
                isLoading = false
            }
        }
    }
    private fun observeMeals() {
        mealRepository.observeMeals { newMeals ->
            mealsList.clear()
            mealsList.addAll(newMeals)
            isLoading = false
        }
    }
    fun findMealById(idMeal: String): Meal? {
        return mealsList.firstOrNull { it.idMeal == idMeal }
    }

}