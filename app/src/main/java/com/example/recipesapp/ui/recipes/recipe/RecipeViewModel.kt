package com.example.recipesapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.example.recipesapp.model.Ingredient

data class RecipeState(
    val name: String? = null,
    val image: String?= null,
    val isFavorite: Boolean = false,
    val numberOfPortions: Int = MIN_AMOUNT_OF_PORTIONS,
    val ingredients: List<Ingredient> = emptyList(),
    val method: List<String> = emptyList(),
)

class RecipeViewModel : ViewModel() {
}