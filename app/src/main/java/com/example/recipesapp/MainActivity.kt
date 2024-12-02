package com.example.recipesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.recipesapp.databinding.ActivityMainBinding

private var _binding: ActivityMainBinding? = null
private val binding
    get() = _binding
        ?: throw IllegalStateException("Binding для ActivityMainBinding не должен быть null")


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.mainContainer, CategoriesListFragment()).commit()
    }
}