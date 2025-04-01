package com.example.recipesapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.RecipesApplication
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.ui.recipes.recipeslist.RecipesListAdapter

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding для FragmentFavoritesBinding не должен быть null")

    private lateinit var viewModel: FavoritesViewModel
    private val favoritesAdapter = RecipesListAdapter(listOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        viewModel = appContainer.favoritesViewModelFactory.create()
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

        initRecycler()

        viewModel.loadFavorites()
        viewModel.favoritesState.observe(viewLifecycleOwner) { state ->
            initUI(state)

            state.toastMessage?.let { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }

            if (state.openRecipe && state.selectedRecipe != null) {
                openRecipe(state.selectedRecipe)
                viewModel.onRecipeOpened()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initRecycler() {
        binding.rvFavorites.adapter = favoritesAdapter

        favoritesAdapter.setOnItemClickListener(
            object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    viewModel.onRecipeClicked(recipeId)
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

    private fun openRecipe(recipe: Recipe) {
        findNavController().navigate(
            FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(
                recipe
            )
        )
    }
}