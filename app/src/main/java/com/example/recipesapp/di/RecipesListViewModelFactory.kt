package com.example.recipesapp.di

import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.ui.recipes.recipeslist.RecipesListViewModel

class RecipesListViewModelFactory(
    private val repository: RecipesRepository
) : Factory<RecipesListViewModel> {

    override fun create(): RecipesListViewModel {
        return RecipesListViewModel(repository)
    }
}