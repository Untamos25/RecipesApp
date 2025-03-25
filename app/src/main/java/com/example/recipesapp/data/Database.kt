package com.example.recipesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipesapp.data.converters.IngredientsConverter
import com.example.recipesapp.data.converters.MethodConverter
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.CategoryRecipeCrossRef
import com.example.recipesapp.model.Recipe

@Database(entities = [Category::class, Recipe::class, CategoryRecipeCrossRef::class], version = 1)
@TypeConverters(IngredientsConverter::class, MethodConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
    abstract fun recipesDao(): RecipesDao
}