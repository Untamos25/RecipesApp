package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Recipe

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val FAVORITES_PREFS = "favorites_prefs"
        const val FAVORITES_KEY = "favorites"
    }

    data class RecipeState(
        val recipe: Recipe? = null,
        val numberOfPortions: Int = MIN_AMOUNT_OF_PORTIONS,
        val isFavorite: Boolean = false,
    )

    private val _recipeState = MutableLiveData<RecipeState>()
    val recipeState: LiveData<RecipeState>
        get() = _recipeState

    init {
        Log.i("!!!", "RecipeViewModel инициализирована")
    }

    fun loadRecipe(recipeId: Int) {
//        TODO Релаизовать загрузку из сети

        val recipe = STUB.getRecipeById(recipeId)
        val favorites = getFavorites()
        val isFavorite = favorites.contains(recipeId.toString())
        val currentPortions = _recipeState.value?.numberOfPortions ?: MIN_AMOUNT_OF_PORTIONS

        _recipeState.value = RecipeState(
            recipe = recipe,
            numberOfPortions = currentPortions,
            isFavorite = isFavorite
        )

        Log.i("!!!", "Рецепт с id $recipeId загружен")
    }

    fun onFavoritesClicked() {
        val currentRecipeId = _recipeState.value?.recipe?.id?.toString() ?: return
        val favorites = getFavorites()
        val isFavorite = favorites.contains(currentRecipeId)

        if (isFavorite) {
            favorites.remove(currentRecipeId)
        } else {
            favorites.add(currentRecipeId)
        }

        _recipeState.value = _recipeState.value?.copy(isFavorite = !isFavorite)
        saveFavorites(favorites)
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