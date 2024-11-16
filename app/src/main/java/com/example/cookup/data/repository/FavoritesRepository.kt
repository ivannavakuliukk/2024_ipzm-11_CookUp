package com.example.cookup.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await


class FavoritesRepository {
    private val database = FirebaseDatabase.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    // Додати рецепт до обраних
    fun addRecipeToFavorites(recipeId: String) {
        userId?.let {
            database.reference.child("favorites").child("users").child(it).child(recipeId)
                .setValue(true)
                .addOnSuccessListener {
                    Log.e("FavoritesRepository", "Recipe added $recipeId")
                }
                .addOnFailureListener { exception ->
                    Log.e("FavoritesRepository", "Error adding favorite $exception")
                }
        }
    }

    // Видалити рецепт з обраних
    fun removeRecipeFromFavorites(recipeId: String) {
        userId?.let {
            database.reference.child("favorites").child("users").child(it).child(recipeId)
                .removeValue()
                .addOnSuccessListener {
                    Log.e("FavoritesRepository", "Recipe deleted $recipeId")
                }
                .addOnFailureListener { exception ->
                    Log.e("FavoritesRepository", "Error deletion favorite $exception")
                }
        }
    }

        // Отримати обрані рецепти користувача
    suspend fun getFavoriteRecipes(): List<String> {
            return userId?.let { id ->
                try {
                    val snapshot = database.reference.child("favorites").child("users").child(id).get().await()
                    // Перетворення snapshot в список
                    snapshot.children.mapNotNull { it.key }
                } catch (e: Exception) {
                    Log.e("FavoritesRepository", "Error fetching favorite recipes", e)
                    emptyList()  // Повертаємо порожній список, якщо є помилка
                }
            } ?: emptyList() // Повертаємо порожній список, якщо userId відсутній
    }
}
