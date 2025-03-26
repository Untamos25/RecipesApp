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
        val selectedRecipe: Recipe? = null,
        val openRecipe: Boolean = false
    )

    private val _recipesListState = MutableLiveData<RecipesListState>()
    val recipesListState: LiveData<RecipesListState>
        get() = _recipesListState

    private val repository = RecipesRepository(application)

    init {
        Log.i("!!! RecipesListViewModel", "RecipesListViewModel инициализирована")
    }

    fun loadRecipesList(categoryId: Int) {
        viewModelScope.launch {
            val cachedCategory = repository.getCategoriesFromCache()
                ?.find { cachedCategory -> cachedCategory.id == categoryId }
            val cachedRecipes = repository.getRecipesForCategoryFromCache(categoryId)

            if (cachedRecipes?.isNotEmpty() == true) {
                _recipesListState.value = recipesListState.value?.copy(
                    category = cachedCategory,
                    recipesList = cachedRecipes
                ) ?: RecipesListState(category = cachedCategory, recipesList = cachedRecipes)
                Log.i(
                    "!!! RecipesListViewModel",
                    "Список рецептов из ${cachedRecipes.size} элементов загружен из кэша"
                )
            }

            Log.i("!!! RecipesListViewModel", "Загружаю список рецептов из сети")
            val backendCategory = repository.getCategoryById(categoryId)
            val backendRecipes = repository.getRecipesByCategoryId(categoryId)

            backendRecipes?.let {
                if (cachedRecipes != backendRecipes) {
                    repository.insertRecipesForCategoryInDatabase(categoryId, backendRecipes)
                    Log.i("!!! RecipesListViewModel", "Рецепты записаны в БД")
                    val updatedRecipes = repository.getRecipesForCategoryFromCache(categoryId)
                    Log.i(
                        "!!! RecipesListViewModel",
                        "Список рецептов категории ${backendCategory?.title} загружен из сети и принял статусы избранного из БД"
                    )

                    _recipesListState.value = recipesListState.value?.copy(
                        category = backendCategory,
                        recipesList = updatedRecipes,
                    ) ?: RecipesListState(
                        category = backendCategory,
                        recipesList = updatedRecipes,
                    )
                    Log.i(
                        "!!! RecipesListViewModel",
                        "UI со списком рецептов обновлён"
                    )
                } else {
                    Log.i(
                        "!!! RecipesListViewModel",
                        "Сетевые данные совпадают с кэшем: UI не обновляется, рецепты в БД не заменяются"
                    )
                }
            }

            if (cachedRecipes.isNullOrEmpty() && backendRecipes.isNullOrEmpty()) {
                Toast.makeText(
                    getApplication(),
                    "Не удалось загрузить рецепты для выбранной категории",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun onRecipeClicked(recipeId: Int) {
        viewModelScope.launch {
            val recipes = _recipesListState.value?.recipesList
            val selectedRecipe = recipes?.find { it.id == recipeId }

            if (selectedRecipe != null) {
                _recipesListState.value = recipesListState.value?.copy(
                    selectedRecipe = selectedRecipe,
                    openRecipe = true
                )
            } else {
                val backendRecipe = repository.getRecipeById(recipeId)

                if (backendRecipe != null) {
                    _recipesListState.value = recipesListState.value?.copy(
                        selectedRecipe = backendRecipe,
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
    }

    fun onRecipeOpened() {
        _recipesListState.value = recipesListState.value?.copy(
            openRecipe = false,
            selectedRecipe = null
        )
    }

}