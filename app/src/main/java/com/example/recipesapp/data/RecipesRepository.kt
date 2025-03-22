package com.example.recipesapp.data

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.example.recipesapp.DataConstants.BASE_URL
import com.example.recipesapp.model.Category
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
    ).build()

    val categoriesDao = database.categoriesDao()


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
            Log.e("!!! RecipesRepository", "Ошибка при записи данных в БД.", e)
            null
        }
    }

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


    suspend fun getRecipesByIds(setIds: String): List<Recipe>? = withContext(dispatcher) {
        try {
            recipeApiService.getRecipesByIds(setIds)
        } catch (e: Exception) {
            Log.e("!!! RecipesRepository", "Ошибка при получении списка рецептов по их id", e)
            null
        }
    }

}