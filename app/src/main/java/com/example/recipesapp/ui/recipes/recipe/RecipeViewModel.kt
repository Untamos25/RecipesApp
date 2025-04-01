package com.example.recipesapp.ui.recipes.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.ModelConstants.MIN_AMOUNT_OF_PORTIONS
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val repository: RecipesRepository,
) : ViewModel() {

    data class RecipeState(
        val recipe: Recipe? = null,
        val numberOfPortions: Int = MIN_AMOUNT_OF_PORTIONS,
        val isFavorite: Boolean = false,
    )

    private val _recipeState = MutableLiveData<RecipeState>()
    val recipeState: LiveData<RecipeState>
        get() = _recipeState

    fun setRecipe(recipe: Recipe) {
        _recipeState.value =
            recipeState.value?.copy(recipe = recipe) ?: RecipeState(recipe = recipe)
    }

    fun onFavoritesClicked() {
        val currentRecipe = _recipeState.value?.recipe ?: return
        val currentRecipeId = currentRecipe.id
        val isCurrentlyFavorite = currentRecipe.isFavorite
        val newFavoriteState = !isCurrentlyFavorite

        viewModelScope.launch {
            repository.updateFavoriteStatusInCache(currentRecipeId, newFavoriteState)

            val updatedRecipe = currentRecipe.copy(isFavorite = newFavoriteState)
            _recipeState.value = recipeState.value?.copy(
                recipe = updatedRecipe
            ) ?: RecipeState(recipe = updatedRecipe)
        }
    }

    fun updateNumberOfPortions(portions: Int) {
        _recipeState.value = recipeState.value?.copy(numberOfPortions = portions)
    }

}