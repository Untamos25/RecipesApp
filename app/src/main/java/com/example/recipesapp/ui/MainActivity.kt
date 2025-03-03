package com.example.recipesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ActivityMainBinding
import com.example.recipesapp.model.Category
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding для ActivityMainBinding не должен быть null")

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }
    private val threadPool = Executors.newFixedThreadPool(10)
    private val dispatcher = Dispatcher(threadPool).apply { maxRequests = 10 }

    private val client = OkHttpClient.Builder()
        .dispatcher(dispatcher)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryRequest = Request.Builder()
            .url("https://recipes.androidsprint.ru/api/category")
            .build()

        client.newCall(categoryRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("!!!", "Ошибка при получении категорий", e)
            }

            override fun onResponse(call: Call, response: Response) {
                val categoryBody = response.body?.string()
                Log.i("!!!", "categoryBody: $categoryBody")

                val categories: List<Category> = Json.decodeFromString(categoryBody ?: "[]")
                Log.i("!!!", "Категории после десериализации: $categories")

                val categoryIds = categories.map { it.id }

                categoryIds.forEach { categoryId ->
                    val recipeRequest = Request.Builder()
                        .url("https://recipes.androidsprint.ru/api/category/${categoryId}/recipes")
                        .build()

                    client.newCall(recipeRequest).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.e("!!!", "Ошибка при получении рецептов для котегории $categoryId", e)
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val recipesBody = response.body?.string()
                            Log.i("!!!", "Рецепты для категории $categoryId: $recipesBody")
                        }
                    })
                }
            }
        })

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
        dispatcher.cancelAll()
        threadPool.shutdown()
    }
}