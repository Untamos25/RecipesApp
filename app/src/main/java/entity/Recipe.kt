package entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: List<Ingredient>,
    val method: String,
    val imageUrl: String,
) : Parcelable