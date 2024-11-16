package com.example.cookup.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookup.data.models.Meal
import com.example.cookup.data.repository.FavoritesRepository
import com.example.cookup.data.repository.MealRepository
import kotlinx.coroutines.launch
import kotlin.time.measureTimedValue

class FavoritesViewModel: ViewModel() {
    private val favoritesRepository = FavoritesRepository()
    private val mealRepository = MealRepository()
    var mealsList = mutableStateListOf<Meal>()
    var favoriteIds = mutableStateListOf<String>()
    // Стан завантаження
    var isLoading by mutableStateOf(false)
        private set
    init {
        loadFavoriteRecipes()
    }
    // Додати рецепт в обрані
    fun addRecipeToFavorites(mealId: String) {
        viewModelScope.launch {
            favoritesRepository.addRecipeToFavorites(mealId)
            mealRepository.fetchMealById(mealId)?.let { mealsList.add(it) }
            favoriteIds.add(mealId)
        }
    }

    // Видалити рецепт з обраних
    fun removeRecipeFromFavorites(mealId: String) {
        viewModelScope.launch {
            favoritesRepository.removeRecipeFromFavorites(mealId)
            mealsList.remove(mealRepository.fetchMealById(mealId))
            favoriteIds.remove(mealId)
        }
    }

    /* Функція для завантаження улюблених рецептів
    З бд ми отримуємо лише айді страв, і щоб отримати об'єкт Meal - треба
    викликати функцію з MealRepository
    */
    private fun loadFavoriteRecipes() {
        isLoading = true
        viewModelScope.launch {
            val favorites = favoritesRepository.getFavoriteRecipes()
            favoriteIds.clear()
            favoriteIds.addAll(favorites)
            val favoriteMeals = favoriteIds.mapNotNull { id ->
                mealRepository.fetchMealById(id)
            }
            mealsList.clear()
            mealsList.addAll(favoriteMeals)
            isLoading = false
        }
    }

}