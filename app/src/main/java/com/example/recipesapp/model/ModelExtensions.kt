package com.example.recipesapp.model

import com.example.recipesapp.DataConstants

fun Category.getFullImageUrl(): String {
    return "${DataConstants.BASE_URL_IMAGES}${this.imageUrl}"
}

fun Recipe.getFullImageUrl(): String {
    return "${DataConstants.BASE_URL_IMAGES}${this.imageUrl}"
}