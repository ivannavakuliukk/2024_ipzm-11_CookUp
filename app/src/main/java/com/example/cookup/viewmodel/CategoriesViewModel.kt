package com.example.cookup.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookup.models.Area
import com.example.cookup.models.Category
import com.example.cookup.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoriesViewModel : ViewModel() {
    private val categoriesList = mutableStateListOf<Category>()
    val categories: List<Category> get() = categoriesList
    private val areasList = mutableStateListOf<Area>()
    val meals: List<Area> get() = areasList
    init {
        fetchCategories()
        fetchAreas()
    }

    // Функція для отримання всіх категорій
    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getCategories()
                }
                categoriesList.clear()
                categoriesList.addAll(response.categories)
            } catch (e: Exception) {
                // Обробка помилки
                e.printStackTrace()
            }
        }
    }

    // Функція для отримання всіх категорій
    private fun fetchAreas() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getAreas()
                }
                areasList.clear()
                areasList.addAll(response.meals)
            } catch (e: Exception) {
                // Обробка помилки
                e.printStackTrace()
            }
        }
    }
}
