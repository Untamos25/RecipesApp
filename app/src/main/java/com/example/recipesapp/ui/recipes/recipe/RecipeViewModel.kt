package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.ModelConstants.MIN_AMOUNT_OF_PORTIONS
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    data class RecipeState(
        val recipe: Recipe? = null,
        val numberOfPortions: Int = MIN_AMOUNT_OF_PORTIONS,
        val isFavorite: Boolean = false,
    )

    private val _recipeState = MutableLiveData<RecipeState>()
    val recipeState: LiveData<RecipeState>
        get() = _recipeState

    private val repository = RecipesRepository(application)

    init {
        Log.i("!!! RecipeViewModel", "RecipeViewModel инициализирована")
    }

    fun setRecipe(recipe: Recipe) {
        _recipeState.value =
            recipeState.value?.copy(recipe = recipe) ?: RecipeState(recipe = recipe)
        Log.i(
            "!!! RecipeViewModel",
            "Рецепт '${recipe.title}' установлен напрямую из аргументов. Избранное: ${recipe.isFavorite}"
        )
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

            Log.i(
                "!!! RecipeViewModel",
                "Статус избранного для ${currentRecipe.title} изменён на $newFavoriteState"
            )
        }
    }

    fun updateNumberOfPortions(portions: Int) {
        _recipeState.value = recipeState.value?.copy(numberOfPortions = portions)
    }

}