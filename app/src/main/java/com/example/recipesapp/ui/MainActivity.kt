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

        val threadName = Thread.currentThread().name

        Log.i("!!!", "Метод onCreate() выполняется на потоке: $threadName")

        val thread = Thread {
            Log.i("!!!", "Выполняю запрос на потоке: $threadName")
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val body = connection.inputStream.bufferedReader().readText()

            Log.i("!!!", "responseCode: ${connection.responseCode}")
            Log.i("!!!", "responseMessage: ${connection.responseMessage}")
            Log.i("!!!", "Body: $body")

            val categories: List<Category> = Json.decodeFromString(body)
            Log.i("!!!", "Категории после десериализации: $categories")

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
    }
}