package com.example.recipesapp.data

import android.util.Log
import com.example.recipesapp.DataConstants.BASE_URL
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class RecipesRepository {

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

    suspend fun getCategories(): List<Category>? {
        return withContext(Dispatchers.IO) {
            try {
                recipeApiService.getCategories().execute().body()
            } catch (e: Exception) {
                Log.e("!!! RecipesRepository", "Ошибка при получении списка категорий", e)
                null
            }
        }
    }


    suspend fun getCategoryById(categoryId: Int): Category? {
        return withContext(Dispatchers.IO) {
            try {
                recipeApiService.getCategoryById(categoryId).execute().body()
            } catch (e: Exception) {
                Log.e("!!! RecipesRepository", "Ошибка при получении категории по её id", e)
                null
            }
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            try {
                recipeApiService.getRecipesByCategoryId(categoryId).execute().body()
            } catch (e: Exception) {
                Log.e(
                    "!!! RecipesRepository",
                    "Ошибка при получении списка рецептов по id категории",
                    e
                )
                null
            }
        }
    }

    suspend fun getRecipeById(recipeId: Int): Recipe? {
        return withContext(Dispatchers.IO) {
            try {
                recipeApiService.getRecipeById(recipeId).execute().body()
            } catch (e: Exception) {
                Log.e("!!! RecipesRepository", "Ошибка при получении рецепта по его id", e)
                null
            }
        }
    }

    suspend fun getRecipesByIds(setIds: String): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            try {
                recipeApiService.getRecipesByIds(setIds).execute().body()
            } catch (e: Exception) {
                Log.e("!!! RecipesRepository", "Ошибка при получении списка рецептов по их id", e)
                null
            }
        }
    }
}