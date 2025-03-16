package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.DataConstants.FAVORITES_KEY
import com.example.recipesapp.DataConstants.FAVORITES_PREFS
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

    private val repository = RecipesRepository()

    init {
        Log.i("!!!", "RecipeViewModel инициализирована")
    }

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            val recipe = repository.getRecipeById(recipeId)
            val isFavorite = getFavorites().contains(recipeId.toString())

            if (recipe != null) {
                _recipeState.value = recipeState.value?.copy(
                    recipe = recipe,
                    isFavorite = isFavorite,
                ) ?: RecipeState(
                    recipe = recipe,
                    isFavorite = isFavorite,
                )
                Log.i("!!!", "Рецепт с id $recipeId загружен")
            } else {
                Toast.makeText(
                    getApplication(),
                    "Не удалось загрузить рецепт",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun onFavoritesClicked() {
        val currentRecipeId = recipeState.value?.recipe?.id?.toString() ?: return
        val favorites = getFavorites()
        val isFavorite = favorites.contains(currentRecipeId)

        if (isFavorite) {
            favorites.remove(currentRecipeId)
        } else {
            favorites.add(currentRecipeId)
        }

        _recipeState.value = recipeState.value?.copy(isFavorite = !isFavorite)
        saveFavorites(favorites)
    }

    fun updateNumberOfPortions(portions: Int) {
        _recipeState.value = recipeState.value?.copy(numberOfPortions = portions)
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs = getApplication<Application>().getSharedPreferences(
            FAVORITES_PREFS,
            Context.MODE_PRIVATE
        )
        val storedSet = sharedPrefs.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
        return HashSet(storedSet)
    }

    private fun saveFavorites(favorites: Set<String>) {
        val sharedPrefs = getApplication<Application>().getSharedPreferences(
            FAVORITES_PREFS,
            Context.MODE_PRIVATE
        )
        with(sharedPrefs.edit()) {
            putStringSet(FAVORITES_KEY, favorites)
            apply()
        }
    }
}