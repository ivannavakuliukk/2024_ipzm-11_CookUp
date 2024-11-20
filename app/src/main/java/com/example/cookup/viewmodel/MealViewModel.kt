package com.example.cookup.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookup.data.models.Meal
import com.example.cookup.data.repository.MealRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel для завантаження рекомендованих страв
class MealViewModel : ViewModel() {
    private val mealRepository = MealRepository()
    val mealsList: List<Meal> get() = mealRepository.recommendedMeals
    var isFirstLoading by mutableStateOf(false)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var isSynchronized by mutableStateOf(false)
        private set
    private var favoriteIds: List<String> = emptyList()

    init {
        fetchRandomMeals()
    }

    // Оновлення списку випадкових страв
    fun fetchRandomMeals() {
        if (mealsList.isEmpty()){
            isFirstLoading = true
        }else{
            isLoading = true
        }
        viewModelScope.launch {
            mealRepository.fetchRandomMeals()
            syncWithFavorites()
            isFirstLoading = false
            isLoading = false
        }
    }

    private fun syncWithFavorites(){
        viewModelScope.launch {
            mealRepository.syncWithFavorites(mealsList, favoriteIds)
        }
    }

    fun syncWithFavorites2(ids: List<String>){
        isSynchronized = true
        viewModelScope.launch {
            mealRepository.syncWithFavorites(mealsList, ids)
            isSynchronized = false
        }
    }

    fun setFavoriteIds(favoriteIds: List<String>) {
        this.favoriteIds = favoriteIds
    }
}

