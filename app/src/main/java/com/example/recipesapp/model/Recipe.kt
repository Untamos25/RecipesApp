package com.example.recipesapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Entity
data class Recipe(
    @PrimaryKey val id: Int,
    val title: String,
    val ingredients: List<Ingredient>,
    val method: List<String>,
    val imageUrl: String,
) : Parcelable