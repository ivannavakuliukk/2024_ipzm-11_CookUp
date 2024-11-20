package com.example.cookup.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookup.data.models.Meal
import com.example.cookup.data.repository.MealRepository
import kotlinx.coroutines.launch
import kotlin.time.measureTimedValue

class FavoritesViewModel: ViewModel() {
    private val mealRepository = MealRepository()
    val mealsList: List<Meal> get() = mealRepository.favoriteMeals
    val favoriteIds: List<String> get() = mealRepository.favoriteIds
    // Стан завантаження
    var isLoading by mutableStateOf(false)
        private set

    init {
        loadFavoriteRecipes()
    }

    // Додати рецепт в обрані
    fun addRecipeToFavorites(mealId: String) {
        viewModelScope.launch {
            mealRepository.addRecipeToFavorites(mealId)
        }
    }

    // Видалити рецепт з обраних
    fun removeRecipeFromFavorites(mealId: String) {
        viewModelScope.launch {
            mealRepository.removeRecipeFromFavorites(mealId)
        }
    }

    // Функція для завантаження улюблених рецептів
    private fun loadFavoriteRecipes() {
        isLoading = true
        viewModelScope.launch {
            mealRepository.getFavoriteRecipes()
            isLoading = false
        }
    }
}