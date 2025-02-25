package com.example.recipesapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding для ActivityMainBinding не должен быть null")

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCategories.setOnClickListener{
            if (navController.currentDestination?.id != R.id.categoriesListFragment) {
                navController.navigate(R.id.global_action_to_categoriesListFragment)
            }
        }

        binding.btnFavourites.setOnClickListener{
            if (navController.currentDestination?.id != R.id.favoritesFragment) {
                navController.navigate(R.id.global_action_to_favoritesFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}