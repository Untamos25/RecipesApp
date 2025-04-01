package com.example.recipesapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    private val repository: RecipesRepository,
) : ViewModel() {

    data class CategoriesListState(
        val categoriesList: List<Category>? = null,
        val selectedCategory: Category? = null,
        val openRecipeList: Boolean = false,
        val toastMessage: String? = null,
    )

    private val _categoriesListState = MutableLiveData<CategoriesListState>()
    val categoriesListState: LiveData<CategoriesListState>
        get() = _categoriesListState


    fun loadCategories() {
        viewModelScope.launch {
            val cachedCategories = repository.getCategoriesFromCache()

            if (cachedCategories?.isNotEmpty() == true) {
                _categoriesListState.value = categoriesListState.value?.copy(
                    categoriesList = cachedCategories
                ) ?: CategoriesListState(categoriesList = cachedCategories)
            }

            val backendCategories = repository.getCategories()

            backendCategories?.let {
                if (cachedCategories != backendCategories) {
                    _categoriesListState.value = categoriesListState.value?.copy(
                        categoriesList = backendCategories
                    ) ?: CategoriesListState(categoriesList = backendCategories)

                    repository.insertCategoriesInDatabase(backendCategories)
                }
            }

            if (backendCategories.isNullOrEmpty() && cachedCategories.isNullOrEmpty()) {
                _categoriesListState.value = categoriesListState.value?.copy(
                    categoriesList = null
                ) ?: CategoriesListState(categoriesList = null)
                showToast("Не удалось загрузить категории")
            }
        }
    }

    fun onCategoryClicked(categoryId: Int) {
        viewModelScope.launch {
            val categories = _categoriesListState.value?.categoriesList
            val selectedCategory = categories?.find { it.id == categoryId }

            if (selectedCategory != null) {
                _categoriesListState.value = categoriesListState.value?.copy(
                    selectedCategory = selectedCategory,
                    openRecipeList = true
                )
            } else {
                val backendCategory = repository.getCategoryById(categoryId)

                if (backendCategory != null) {
                    _categoriesListState.value = categoriesListState.value?.copy(
                        selectedCategory = backendCategory,
                        openRecipeList = true
                    )
                } else {
                    showToast("Не удалось загрузить рецепты для выбранной категории")
                }
            }
        }
    }

    fun onRecipeListOpened() {
        _categoriesListState.value = categoriesListState.value?.copy(
            openRecipeList = false,
            selectedCategory = null
        )
    }

    private fun showToast(message: String) {
        _categoriesListState.value = categoriesListState.value?.copy(
            toastMessage = message
        )
        _categoriesListState.value = categoriesListState.value?.copy(
            toastMessage = null
        )
    }

}