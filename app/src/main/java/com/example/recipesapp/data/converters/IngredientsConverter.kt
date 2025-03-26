package com.example.recipesapp.data.converters

import androidx.room.TypeConverter
import com.example.recipesapp.model.Ingredient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class IngredientsConverter {

    @TypeConverter
    fun fromIngredientsList(ingredients: List<Ingredient>): String {
        return Json.encodeToString(ingredients)
    }

    @TypeConverter
    fun toIngredientsList(ingredientsString: String): List<Ingredient> {
        return Json.decodeFromString(ingredientsString)
    }
}