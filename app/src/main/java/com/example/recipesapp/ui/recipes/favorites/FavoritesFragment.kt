package com.example.recipesapp.ui.recipes.favorites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipesapp.R
import com.example.recipesapp.data.STUB
import com.example.recipesapp.ui.recipes.recipe.RecipeFragment.Companion.FAVORITES_KEY
import com.example.recipesapp.ui.recipes.recipe.RecipeFragment.Companion.FAVORITES_PREFS
import com.example.recipesapp.ui.recipes.recipeslist.RecipesListFragment.Companion.ARG_RECIPE
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.ui.recipes.recipe.RecipeFragment
import com.example.recipesapp.ui.recipes.recipeslist.RecipesListAdapter

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding для FragmentFavoritesBinding не должен быть null")

    private val sharedPrefs by lazy {
        requireActivity().getSharedPreferences(FAVORITES_PREFS, Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favorites = getFavorites()
        if (favorites.isNotEmpty()) {
            binding.tvEmptyFavorites.visibility = View.GONE
            binding.rvFavorites.visibility = View.VISIBLE
            initRecycler(favorites)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler(favorites: MutableSet<String>) {
        val favoritesAdapter = RecipesListAdapter(STUB.getRecipesByIds(favorites))
        binding.rvFavorites.adapter = favoritesAdapter

        favoritesAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {

            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)

        val bundle = bundleOf(
            ARG_RECIPE to recipe
        )

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
        }
    }

    private fun getFavorites(): MutableSet<String> {
        val storedSet = sharedPrefs.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
        return HashSet(storedSet)
    }
}