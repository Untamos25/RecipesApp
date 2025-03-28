package com.example.recipesapp.data

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.example.recipesapp.DataConstants.BASE_URL
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.CategoryRecipeCrossRef
import com.example.recipesapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class RecipesRepository(
    application: Application,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val contentType = "application/json".toMediaType()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    private val recipeApiService: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    val database = Room.databaseBuilder(
        application.applicationContext,
        Database::class.java,
        "database"
    ).fallbackToDestructiveMigration().build()

    val categoriesDao = database.categoriesDao()
    val recipesDao = database.recipesDao()


    // Методы для работы с БД

    suspend fun getCategoriesFromCache() = withContext(dispatcher) {
        try {
            categoriesDao.getAllCategories()
        } catch (e: Exception) {
            Log.e("!!! RecipesRepository", "Ошибка при получении списка категорий из кэша", e)
            null
        }
    }


    suspend fun insertCategoriesInDatabase(categories: List<Category>) = withContext(dispatcher) {
        try {
            categoriesDao.insertCategories(categories)
        } catch (e: Exception) {
            Log.e("!!! RecipesRepository", "Ошибка при записи категорий в БД.", e)
            null
        }
    }


    suspend fun getRecipesForCategoryFromCache(categoryId: Int): List<Recipe>? =
        withContext(dispatcher) {
            try {
                recipesDao.getRecipesForCategory(categoryId)
            } catch (e: Exception) {
                Log.e(
                    "!!! RecipesRepository",
                    "Ошибка при получении рецептов из кэша для категории $categoryId",
                    e
                )
                null
            }
        }


    suspend fun insertRecipesForCategoryInDatabase(categoryId: Int, recipes: List<Recipe>) =
        withContext(dispatcher) {
            try {
                val favoriteRecipes = recipesDao.getFavoriteRecipes()
                val favoriteRecipeIds = favoriteRecipes.map { it.id }.toSet()
                val recipesWithFavorites = recipes.map { recipe ->
                    recipe.copy(isFavorite = favoriteRecipeIds.contains(recipe.id))
                }

                recipesDao.insertRecipes(recipesWithFavorites)

                val crossRefs = recipes.map { recipe ->
                    CategoryRecipeCrossRef(categoryId, recipe.id)
                }
                recipesDao.insertCategoryRecipeCrossRef(crossRefs)
            } catch (e: Exception) {
                Log.e("!!! RecipesRepository", "Ошибка при записи рецептов в БД.", e)
            }
        }


    suspend fun getFavoriteRecipesFromCache(): List<Recipe>? = withContext(dispatcher) {
        try {
            recipesDao.getFavoriteRecipes()
        } catch (e: Exception) {
            Log.e("!!! RecipesRepository", "Ошибка при получении избранных рецептов из кэша", e)
            null
        }
    }

    suspend fun updateFavoriteStatusInCache(recipeId: Int, isFavorite: Boolean) = withContext(dispatcher) {
        try {
            recipesDao.updateFavoriteStatus(recipeId, isFavorite)
            Log.i("!!! RecipesRepository", "Статус избранного для $recipeId обновлен на $isFavorite в БД")
        } catch (e: Exception) {
            Log.e("!!! RecipesRepository", "Ошибка при обновлении статуса избранного для $recipeId в БД", e)
        }
    }


    // Методы для работы с API

    suspend fun getCategories(): List<Category>? = withContext(dispatcher) {
        try {
            recipeApiService.getCategories()
        } catch (e: Exception) {
            Log.e("!!! RecipesRepository", "Ошибка при получении списка категорий", e)
            null
        }
    }

    suspend fun getCategoryById(categoryId: Int): Category? = withContext(dispatcher) {
        try {
            recipeApiService.getCategoryById(categoryId)
        } catch (e: Exception) {
            Log.e("!!! RecipesRepository", "Ошибка при получении категории по её id", e)
            null
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? = withContext(dispatcher) {
        try {
            recipeApiService.getRecipesByCategoryId(categoryId)
        } catch (e: Exception) {
            Log.e(
                "!!! RecipesRepository",
                "Ошибка при получении списка рецептов по id категории",
                e
            )
            null
        }
    }

    suspend fun getRecipeById(recipeId: Int): Recipe? = withContext(dispatcher) {
        try {
            recipeApiService.getRecipeById(recipeId)
        } catch (e: Exception) {
            Log.e("!!! RecipesRepository", "Ошибка при получении рецепта по его id", e)
            null
        }
    }

}