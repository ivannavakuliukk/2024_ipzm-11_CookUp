package com.example.cookup.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookup.data.models.Meal
import com.example.cookup.data.repository.MealRepository
import kotlinx.coroutines.launch

// ViewModel для завантаження рекомендованих страв
class MealViewModel : ViewModel() {
    private val mealRepository = MealRepository()
    val mealsList: List<Meal> get() = mealRepository.recommendedMeals
    var isLoading by mutableStateOf(false)
        private set

    init {
        fetchRandomMeals()
    }

    // Оновлення списку випадкових страв
    private fun fetchRandomMeals() {
        isLoading = true
        viewModelScope.launch {
            mealRepository.fetchRandomMeals()
            syncWithFavorites()
            isLoading = false
        }
    }

    fun syncWithFavorites(){
        isLoading = true
        viewModelScope.launch {
            mealRepository.syncWithFavorites(mealsList)
            isLoading = false
        }
    }
}

