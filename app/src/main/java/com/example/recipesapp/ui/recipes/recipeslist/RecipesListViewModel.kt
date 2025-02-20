package com.example.recipesapp.ui.recipes.recipeslist

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import java.io.InputStream

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    data class RecipesListState(
        val category: Category? = null,
        val recipesList: List<Recipe>? = null,
        val categoryImage: Drawable? = null,
    )

    private val _recipesListState = MutableLiveData<RecipesListState>()
    val recipesListState: LiveData<RecipesListState>
        get() = _recipesListState

    init {
        Log.i("!!!", "RecipesListViewModel инициализирована")
    }

    fun loadRecipesList(categoryId: Int) {
//        TODO Релаизовать загрузку из сети

        val category = STUB.getCategoryByCategoryId(categoryId)
        val recipesList = STUB.getRecipesByCategoryId(categoryId)
        var categoryImage: Drawable? = null
        category?.imageUrl?.let { imageUrl ->
            categoryImage = loadImageFromAssets(imageUrl)
        }

        _recipesListState.value = recipesListState.value?.copy(
            category = category,
            recipesList = recipesList,
            categoryImage = categoryImage
        ) ?: RecipesListState(category = category, recipesList = recipesList, categoryImage = categoryImage)

        Log.i("!!!", "Список рецептов категории ${category?.title} загружен")
    }

    private fun loadImageFromAssets(imageUrl: String): Drawable? {
        return try {
            val inputStream: InputStream = getApplication<Application>().assets.open(imageUrl)
            Drawable.createFromStream(inputStream, null)
        } catch (e: Exception) {
            Log.e("!!!", "Ошибка загрузки изображения: $imageUrl")
            null
        }
    }

}