package com.example.recipesapp.ui.categories

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Category
import kotlinx.coroutines.launch

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {

    data class CategoriesListState(
        val categoriesList: List<Category>? = null,
        val selectedCategory: Category? = null,
        val openRecipeList: Boolean = false
    )

    private val _categoriesListState = MutableLiveData<CategoriesListState>()
    val categoriesListState: LiveData<CategoriesListState>
        get() = _categoriesListState

    private val repository = RecipesRepository(application)

    init {
        Log.i("!!!", "CategoriesViewModel инициализирована")
    }

    fun loadCategories() {
        viewModelScope.launch {
            var categoriesList: List<Category>? = repository.getCategoriesFromCache()

            if (categoriesList?.isNotEmpty() == true) {
                Log.i("!!! CategoriesListViewModel", "Категории загружены из кэша")
                _categoriesListState.value = categoriesListState.value?.copy(
                    categoriesList = categoriesList
                ) ?: CategoriesListState(categoriesList = categoriesList)
                Log.i(
                    "!!!",
                    "Список категорий из ${categoriesList.size} элементов загружен из кэша"
                )
            }

            Log.i("!!! CategoriesListViewModel", "Загружаю категории из сети")
            categoriesList = repository.getCategories()

            categoriesList?.let {
                Log.i("!!! CategoriesListViewModel", "Категории загружены из сети")
                repository.insertCategoriesInDatabase(categoriesList)
                Log.i("!!! CategoriesListViewModel", "Категории записаны в БД")
            }


            if (categoriesList != null) {
                _categoriesListState.value = categoriesListState.value?.copy(
                    categoriesList = categoriesList
                ) ?: CategoriesListState(categoriesList = categoriesList)
                Log.i("!!!", "Список категорий из ${categoriesList.size} элементов загружен из сети")
            } else {
                Toast.makeText(
                    getApplication(),
                    "Не удалось загрузить категории",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun onCategoryClicked(categoryId: Int) {
        viewModelScope.launch {
            val selectedCategory = repository.getCategoryById(categoryId)

            selectedCategory?.let { category ->
                _categoriesListState.value = categoriesListState.value?.copy(
                    selectedCategory = category,
                    openRecipeList = true
                )
                Log.i("!!!", "Произведён переход в категорию ${category.title}")
            }
        }
    }

    fun onRecipeListOpened() {
        _categoriesListState.value = categoriesListState.value?.copy(
            openRecipeList = false,
            selectedCategory = null
        )
    }

}
