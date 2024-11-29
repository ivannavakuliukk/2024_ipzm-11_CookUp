package com.example.cookup.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.cookup.data.models.Meal
import com.example.cookup.data.repository.MealRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookup.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ProfileViewModel:  ViewModel(){
    private val mealRepository = MealRepository()
    val mealsList = mutableStateListOf<Meal>()
    var isLoading by mutableStateOf(false)
        var isMealAdded by mutableStateOf(false)
            private set

    init {
        getMeals()
    }

    // Метод для додавання рецепту
    fun addMeal(meal: Meal) {
        viewModelScope.launch {
            try {
                mealRepository.addMealToFirebase(meal)
                isMealAdded = true
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error adding meal")
            }
        }
    }

    // Метод для отримання всіх рецептів
    fun getMeals() {
        isLoading = true
        isMealAdded = false
        viewModelScope.launch {
            try {
                mealsList.clear()
                mealsList.addAll(mealRepository.getMealsFromFirebase())
                isLoading = false
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error getting meals")
                isLoading = false
            }
        }
    }

    fun uploadImage(imageUri: Uri, onResult: (String?) -> Unit) {
        viewModelScope.launch{
            try {
                mealRepository.uploadImageToFirebase(imageUri, onResult)
            }catch (e: Exception){
                Log.e("ProfileViewModel", "Error uploading image")
            }
        }
    }

    fun logout(context: Context) {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(context, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}