package com.example.recipesapp.data

import android.util.Log
import com.example.recipesapp.DataConstants.BASE_URL
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import java.util.concurrent.Executors

class RecipesRepository {

    private val contentType = "application/json".toMediaType()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    private val recipeApiService: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    val threadPool = Executors.newFixedThreadPool(10)

    fun getCategories(): List<Category>? {
        return try {
            recipeApiService.getCategories().execute().body()
        } catch (e: Exception) {
            Log.e("!!! RecipesRepository", "Ошибка при получении списка категорий", e)
            null
        }
    }

    fun getCategoryById(categoryId: Int): Category? {
        return try {
            recipeApiService.getCategoryById(categoryId).execute().body()
        } catch (e: Exception) {
            Log.e("!!! RecipesRepository", "Ошибка при получении категории по её id", e)
            null
        }
    }

    fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        return try {
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

    fun getRecipeById(recipeId: Int): Recipe? {
        return try {
            recipeApiService.getRecipeById(recipeId).execute().body()
        } catch (e: Exception) {
            Log.e("!!! RecipesRepository", "Ошибка при получении рецепта по его id", e)
            null
        }
    }

    fun getRecipesByIds(setIds: String): List<Recipe>? {
        return try {
            recipeApiService.getRecipesByIds(setIds).execute().body()
        } catch (e: Exception) {
            Log.e("!!! RecipesRepository", "Ошибка при получении списка рецептов по их id", e)
            null
        }
    }
}