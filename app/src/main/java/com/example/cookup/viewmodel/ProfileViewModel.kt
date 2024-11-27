package com.example.cookup.viewmodel

import androidx.compose.runtime.mutableStateListOf
import com.example.cookup.data.models.Meal
import com.example.cookup.data.repository.MealRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ProfileViewModel:  ViewModel(){
    private val mealRepository = MealRepository()
    val mealsList = mutableStateListOf<Meal>()

    init {
        getMeals()
    }

    // Метод для додавання рецепту
    fun addMeal(meal: Meal) {
        viewModelScope.launch {
            try {
                mealRepository.addMealToFirebase(meal)
            } catch (e: Exception) {
                // Обробка помилки при додаванні рецепту
            }
        }
    }

    // Метод для отримання всіх рецептів
    fun getMeals() {
        viewModelScope.launch {
            try {
                mealsList.clear()
                mealsList.addAll(mealRepository.getMealsFromFirebase())
            } catch (e: Exception) {
                // Обробка помилки при отриманні рецептів
            }
        }
    }
}