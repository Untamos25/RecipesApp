package com.example.recipesapp.ui.recipes.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: RecipesRepository,
) : ViewModel() {

    data class FavoritesState(
        val favoritesList: List<Recipe>? = null,
        val selectedRecipe: Recipe? = null,
        val openRecipe: Boolean = false,
        val toastMessage: String? = null,
    )

    private val _favoritesState = MutableLiveData<FavoritesState>()
    val favoritesState: LiveData<FavoritesState>
        get() = _favoritesState

    fun loadFavorites() {
        viewModelScope.launch {
            val favoritesList = repository.getFavoriteRecipesFromCache()

            _favoritesState.value = favoritesState.value?.copy(
                favoritesList = favoritesList
            ) ?: FavoritesState(favoritesList = favoritesList)
        }
    }

    fun onRecipeClicked(recipeId: Int) {
        viewModelScope.launch {
            val recipes = _favoritesState.value?.favoritesList
            val selectedRecipe = recipes?.find { it.id == recipeId }

            if (selectedRecipe != null) {
                _favoritesState.value = favoritesState.value?.copy(
                    selectedRecipe = selectedRecipe,
                    openRecipe = true
                )
            } else {
                showToast("Не удалось загрузить выбранный рецепт")
            }
        }
    }

    fun onRecipeOpened() {
        _favoritesState.value = favoritesState.value?.copy(
            openRecipe = false,
            selectedRecipe = null
        )
    }

    private fun showToast(message: String) {
        _favoritesState.value = favoritesState.value?.copy(
            toastMessage = message
        )
        _favoritesState.value = favoritesState.value?.copy(
            toastMessage = null
        )
    }

}