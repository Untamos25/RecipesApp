package com.example.recipesapp.ui.recipes.recipeslist

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    data class RecipesListState(
        val category: Category? = null,
        val recipesList: List<Recipe>? = null,
    )

    private val _recipesListState = MutableLiveData<RecipesListState>()
    val recipesListState: LiveData<RecipesListState>
        get() = _recipesListState

    private val repository = RecipesRepository(application)

    init {
        Log.i("!!!", "RecipesListViewModel инициализирована")
    }

    fun loadRecipesList(categoryId: Int) {

        viewModelScope.launch {
            val category = repository.getCategoryById(categoryId)
            val recipesList = repository.getRecipesByCategoryId(categoryId)

            if (category != null) {

                if (recipesList != null) {
                    _recipesListState.value = recipesListState.value?.copy(
                        category = category,
                        recipesList = recipesList,
                    ) ?: RecipesListState(
                        category = category,
                        recipesList = recipesList,
                    )
                }
                Log.i("!!!", "Список рецептов категории ${category.title} загружен")
            } else {
                Toast.makeText(
                    getApplication(),
                    "Не удалось загрузить список рецептов",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}