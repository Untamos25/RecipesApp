package com.example.recipesapp.ui.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.data.STUB.getCategories
import com.example.recipesapp.data.STUB.getCategoryByCategoryId
import com.example.recipesapp.model.Category

class CategoriesListViewModel : ViewModel() {

    data class CategoriesListState(
        val categoriesList: List<Category>? = null,
        val selectedCategory: Category? = null
    )

    private val _categoriesListState = MutableLiveData<CategoriesListState>()
    val categoriesListState: LiveData<CategoriesListState>
        get() = _categoriesListState


    init {
        Log.i("!!!", "CategoriesViewModel инициализирована")
    }

    fun loadCategories() {
        val categoriesList = getCategories()

        _categoriesListState.value = categoriesListState.value?.copy(
            categoriesList = categoriesList
        ) ?: CategoriesListState(categoriesList = categoriesList)
        Log.i("!!!", "Список категорий из ${categoriesList.size} элементов загружен")
    }

    fun onCategoryClicked(categoryId: Int) {
        val selectedCategory = getCategoryByCategoryId(categoryId)

        selectedCategory?.let { selectedCategory ->
            _categoriesListState.value = categoriesListState.value?.copy(
                selectedCategory = selectedCategory
            )
        }
        Log.i("!!!", "Произведён переход в категорию ${selectedCategory?.title}")
    }

}