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
    private var favoriteIds: List<String> = emptyList()

    init {
        fetchRandomMeals()
    }

    // Оновлення списку випадкових страв
    fun fetchRandomMeals() {
        isLoading = true
        viewModelScope.launch {
            mealRepository.fetchRandomMeals()
            syncWithFavorites(favoriteIds)
            isLoading = false
        }
    }

    fun syncWithFavorites(ids: List<String>){
        isLoading = true
        viewModelScope.launch {
            mealRepository.syncWithFavorites(mealsList, ids)
            isLoading = false
        }
    }

    fun setFavoriteIds(favoriteIds: List<String>) {
        this.favoriteIds = favoriteIds
    }
}

