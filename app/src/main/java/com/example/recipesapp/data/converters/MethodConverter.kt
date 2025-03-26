package com.example.recipesapp.data.converters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MethodConverter {

    @TypeConverter
    fun fromMethodList(method: List<String>): String {
        return Json.encodeToString(method)
    }

    @TypeConverter
    fun toMethodList(methodString: String): List<String> {
        return Json.decodeFromString(methodString)
    }
}