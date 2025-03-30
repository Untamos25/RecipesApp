package com.example.recipesapp.ui.recipes.recipeslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipesListViewModel(
    private val repository: RecipesRepository,
) : ViewModel() {

    data class RecipesListState(
        val category: Category? = null,
        val recipesList: List<Recipe>? = null,
        val selectedRecipe: Recipe? = null,
        val openRecipe: Boolean = false,
        val toastMessage: String? = null,
    )

    private val _recipesListState = MutableLiveData<RecipesListState>()
    val recipesListState: LiveData<RecipesListState>
        get() = _recipesListState

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
            }

            val backendCategory = repository.getCategoryById(categoryId)
            val backendRecipes = repository.getRecipesByCategoryId(categoryId)

            backendRecipes?.let {
                if (cachedRecipes != backendRecipes) {
                    repository.insertRecipesForCategoryInDatabase(categoryId, backendRecipes)
                    val updatedRecipes = repository.getRecipesForCategoryFromCache(categoryId)

                    _recipesListState.value = recipesListState.value?.copy(
                        category = backendCategory,
                        recipesList = updatedRecipes,
                    ) ?: RecipesListState(
                        category = backendCategory,
                        recipesList = updatedRecipes,
                    )
                }
            }

            if (cachedRecipes.isNullOrEmpty() && backendRecipes.isNullOrEmpty()) {
                _recipesListState.value = recipesListState.value?.copy(
                    recipesList = null
                ) ?: RecipesListState(recipesList = null)
                showToast("Не удалось загрузить рецепты")
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
                    showToast("Не удалось загрузить выбранный рецепт")
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

    private fun showToast(message: String) {
        _recipesListState.value = recipesListState.value?.copy(
            toastMessage = message
        )
        _recipesListState.value = recipesListState.value?.copy(
            toastMessage = null
        )
    }

}