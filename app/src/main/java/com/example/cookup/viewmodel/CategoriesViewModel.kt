package com.example.cookup.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookup.data.models.Area
import com.example.cookup.data.models.Category
import com.example.cookup.data.repository.MealRepository
import kotlinx.coroutines.launch

// ViewModel для категорій та областей
class CategoriesViewModel : ViewModel() {
    private val mealRepository = MealRepository()

    private val categoriesList = mutableStateListOf<Category>()
    val categories: List<Category> get() = categoriesList

    private val areasList = mutableStateListOf<Area>()
    val meals: List<Area> get() = areasList

    init {
        fetchCategories()
        fetchAreas()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            val fetchedCategories = mealRepository.fetchCategories()
            categoriesList.clear()
            categoriesList.addAll(fetchedCategories)
        }
    }

    private fun fetchAreas() {
        viewModelScope.launch {
            val fetchedAreas = mealRepository.fetchAreas()
            areasList.clear()
            areasList.addAll(fetchedAreas)
        }
    }
}
