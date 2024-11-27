package com.example.cookup.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookup.data.models.Meal
import com.example.cookup.data.repository.MealRepository
import kotlinx.coroutines.launch

// ViewModel для завантаження страв за пошуковим запитом
class MealSearchViewModel : ViewModel() {
    private val mealRepository = MealRepository()

    // Стан завантаження
    var isLoading by mutableStateOf(false)
        private set
    var mealsList = mutableStateListOf<Meal>()
        private set
    var isSynchronized by mutableStateOf(false)
        private set

    // Функція для пошуку страв за запитом
    fun searchMeals(query: String, ids: List<String>) {
        if (query.length < 3) return // Перевірка мінімальної довжини запиту
        isLoading = true
        viewModelScope.launch {
            val fetchedMeals = mealRepository.searchMeals(query)
            mealRepository.syncWithFavorites(fetchedMeals, ids)
            mealsList.clear()
            mealsList.addAll(fetchedMeals)
            isLoading = false
        }
    }
    fun syncWithFavorites(ids: List<String>){
        isSynchronized = true
        viewModelScope.launch {
            mealRepository.syncWithFavorites(mealsList, ids)
            isSynchronized = false
        }
    }
}