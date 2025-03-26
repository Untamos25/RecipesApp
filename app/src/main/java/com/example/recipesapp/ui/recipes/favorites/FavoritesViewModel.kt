package com.example.recipesapp.ui.recipes.favorites

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    data class FavoritesState(
        val favoritesList: List<Recipe>? = null,
        val selectedRecipe: Recipe? = null,
        val openRecipe: Boolean = false
    )

    private val _favoritesState = MutableLiveData<FavoritesState>()
    val favoritesState: LiveData<FavoritesState>
        get() = _favoritesState

    private val repository = RecipesRepository(application)

    init {
        Log.i("!!!", "FavoritesViewModel инициализирована")
    }

    fun loadFavorites() {
        viewModelScope.launch {
            val favoritesList = repository.getFavoriteRecipesFromCache()

            _favoritesState.value = favoritesState.value?.copy(
                favoritesList = favoritesList
            ) ?: FavoritesState(favoritesList = favoritesList)
            Log.i("!!!", "Список избранного из ${favoritesList?.size} элементов загружен")
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
                Toast.makeText(
                    getApplication(),
                    "Не удалось загрузить выбранный рецепт",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun onRecipeOpened() {
        _favoritesState.value = favoritesState.value?.copy(
            openRecipe = false,
            selectedRecipe = null
        )
    }

}