package com.example.recipesapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.UiConstants.ARG_RECIPE_ID
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.ui.recipes.recipeslist.RecipesListAdapter

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding для FragmentFavoritesBinding не должен быть null")

    private val viewModel: FavoritesViewModel by viewModels()
    private val favoritesAdapter = RecipesListAdapter(listOf())

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

        initRecycler()

        viewModel.loadFavorites()
        viewModel.favoritesState.observe(viewLifecycleOwner) { state ->
            initUI(state)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initRecycler() {
        binding.rvFavorites.adapter = favoritesAdapter

        favoritesAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {

            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun initUI(state: FavoritesViewModel.FavoritesState) {
        val favoritesList = state.favoritesList

        favoritesList?.let {
            if (favoritesList.isNotEmpty()) {
                binding.tvEmptyFavorites.visibility = View.GONE
                binding.rvFavorites.visibility = View.VISIBLE
                favoritesAdapter.submitList(state.favoritesList)
            }
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = bundleOf(
            ARG_RECIPE_ID to recipeId
        )

        findNavController().navigate(R.id.action_favoritesFragment_to_recipeFragment, bundle)
    }
}