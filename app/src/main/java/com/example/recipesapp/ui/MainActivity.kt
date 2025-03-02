package com.example.recipesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ActivityMainBinding
import com.example.recipesapp.model.Category
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding для ActivityMainBinding не должен быть null")

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    private val threadPool = Executors.newFixedThreadPool(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        val thread = Thread {
            Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
            val categoryUrl = URL("https://recipes.androidsprint.ru/api/category")
            val categoryConnection = categoryUrl.openConnection() as HttpURLConnection
            categoryConnection.connect()
            val categoryBody = categoryConnection.inputStream.bufferedReader().readText()

            Log.i("!!!", "responseCode: ${categoryConnection.responseCode}")
            Log.i("!!!", "responseMessage: ${categoryConnection.responseMessage}")
            Log.i("!!!", "Body: $categoryBody")

            val categories: List<Category> = Json.decodeFromString(categoryBody)
            Log.i("!!!", "Категории после десериализации: $categories")

            val categoryIds = categories.map { it.id }

            categoryIds.forEach { categoryId ->
                threadPool.execute {
                        val recipeUrl = URL("https://recipes.androidsprint.ru/api/category/${categoryId}/recipes")
                        val recipeConnection = recipeUrl.openConnection() as HttpURLConnection
                        recipeConnection.connect()
                        val recipeBody = recipeConnection.inputStream.bufferedReader().readText()

                        Log.i("!!!", "Поток [${Thread.currentThread().name}]\nРецепты для категории с id $categoryId: $recipeBody")
                }
            }
        }

        thread.start()


        binding.btnCategories.setOnClickListener {
            if (navController.currentDestination?.id != R.id.categoriesListFragment) {
                navController.navigate(R.id.global_action_to_categoriesListFragment)
            }
        }

        binding.btnFavourites.setOnClickListener {
            if (navController.currentDestination?.id != R.id.favoritesFragment) {
                navController.navigate(R.id.global_action_to_favoritesFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        threadPool.shutdown()
    }
}