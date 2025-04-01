package com.example.recipesapp.di

import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.ui.recipes.favorites.FavoritesViewModel

class FavoritesViewModelFactory(
    private val repository: RecipesRepository
) : Factory<FavoritesViewModel> {

    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(repository)
    }
}