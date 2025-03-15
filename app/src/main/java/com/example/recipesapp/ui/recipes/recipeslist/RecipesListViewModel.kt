package com.example.recipesapp.ui.recipes.recipeslist

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    data class RecipesListState(
        val category: Category? = null,
        val recipesList: List<Recipe>? = null,
    )

    private val _recipesListState = MutableLiveData<RecipesListState>()
    val recipesListState: LiveData<RecipesListState>
        get() = _recipesListState

    private val repository = RecipesRepository()

    init {
        Log.i("!!!", "RecipesListViewModel инициализирована")
    }

    fun loadRecipesList(categoryId: Int) {

        repository.threadPool.execute {
            val category = repository.getCategoryById(categoryId)
            val recipesList = repository.getRecipesByCategoryId(categoryId)

            if (category != null) {

                if (recipesList != null) {
                    _recipesListState.postValue(
                        recipesListState.value?.copy(
                            category = category,
                            recipesList = recipesList,
                        ) ?: RecipesListState(
                            category = category,
                            recipesList = recipesList,
                        )
                    )
                }

                Log.i("!!!", "Список рецептов категории ${category.title} загружен")
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        getApplication(),
                        "Не удалось загрузить список рецептов",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}