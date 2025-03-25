package com.example.recipesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipesapp.model.CategoryRecipeCrossRef
import com.example.recipesapp.model.Recipe

@Dao
interface RecipesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryRecipeCrossRef(crossRefs: List<CategoryRecipeCrossRef>)

    @Query(
        """
        SELECT Recipe.* FROM Recipe 
        INNER JOIN CategoryRecipeCrossRef ON Recipe.id = CategoryRecipeCrossRef.recipeId 
        WHERE CategoryRecipeCrossRef.categoryId = :categoryId
        """
    )
    suspend fun getRecipesForCategory(categoryId: Int): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<Recipe>)

    @Query("SELECT * FROM Recipe WHERE id = :recipeId")
    suspend fun getRecipeById(recipeId: Int): Recipe?
}