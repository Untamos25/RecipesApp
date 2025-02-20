package com.example.recipesapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.DataConstants.FAVORITES_KEY
import com.example.recipesapp.DataConstants.FAVORITES_PREFS
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Recipe

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    data class FavoritesState(
        val favoritesList: List<Recipe>? = null,
    )

    private val _favoritesState = MutableLiveData<FavoritesState>()
    val favoritesState: LiveData<FavoritesState>
        get() = _favoritesState

    init {
        Log.i("!!!", "FavoritesViewModel инициализирована")
    }

    fun loadFavorites() {
//        TODO Релаизовать загрузку из сети

        val favoritesIdsList = getFavorites()
        val favoritesList = STUB.getRecipesByIds(favoritesIdsList)

        _favoritesState.value = favoritesState.value?.copy(
            favoritesList = favoritesList
        ) ?: FavoritesState(favoritesList = favoritesList)

        Log.i("!!!", "Список избранного из ${favoritesList.size} элементов загружен")
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs = getApplication<Application>().getSharedPreferences(
            FAVORITES_PREFS,
            Context.MODE_PRIVATE
        )
        val storedSet = sharedPrefs.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
        return HashSet(storedSet)
    }

}