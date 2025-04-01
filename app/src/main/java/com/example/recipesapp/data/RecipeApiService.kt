package com.example.recipesapp.data

import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeApiService {
    @GET("category")
    suspend fun getCategories(): List<Category>

    @GET("category/{id}")
    suspend fun getCategoryById(@Path("id") id: Int): Category

    @GET("category/{id}/recipes")
    suspend fun getRecipesByCategoryId(@Path("id") id: Int): List<Recipe>

    @GET("recipe/{id}")
    suspend fun getRecipeById(@Path("id") id: Int): Recipe
}
