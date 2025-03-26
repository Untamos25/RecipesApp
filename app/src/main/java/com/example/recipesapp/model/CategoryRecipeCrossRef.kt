package com.example.recipesapp.model

import androidx.room.Entity

@Entity(primaryKeys = ["categoryId", "recipeId"])
data class CategoryRecipeCrossRef(
    val categoryId: Int,
    val recipeId: Int
)