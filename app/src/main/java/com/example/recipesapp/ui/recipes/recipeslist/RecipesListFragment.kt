package com.example.recipesapp.ui.recipes.recipeslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.RecipesApplication
import com.example.recipesapp.databinding.FragmentListRecipesBinding
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.model.getFullImageUrl
import java.lang.IllegalStateException

class RecipesListFragment : Fragment() {

    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding для FragmentListRecipesBinding не должен быть null")

    private lateinit var viewModel: RecipesListViewModel
    private val recipesListFragmentArgs: RecipesListFragmentArgs by navArgs()
    private val recipesListAdapter = RecipesListAdapter(listOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        viewModel = appContainer.recipesListViewModelFactory.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedINstanceState: Bundle?
    ): View {
        _binding = FragmentListRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryId = recipesListFragmentArgs.category.id

        initRecycler()

        viewModel.loadRecipesList(categoryId)
        viewModel.recipesListState.observe(viewLifecycleOwner) { state ->
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
        with(binding) {
            rvRecipes.layoutManager = LinearLayoutManager(requireContext())
            rvRecipes.adapter = recipesListAdapter
        }

        recipesListAdapter.setOnItemClickListener(
            object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    viewModel.onRecipeClicked(recipeId)
                }
            })
    }

    private fun initUI(state: RecipesListViewModel.RecipesListState) {
        val category = state.category
        val recipesList = state.recipesList

        with(binding) {
            category?.let {
                tvCategory.text = it.title

                Glide.with(this@RecipesListFragment)
                    .load(category.getFullImageUrl())
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_error)
                    .into(imgCategory)

                imgCategory.contentDescription = getString(R.string.category_image, state.category.title)
            }

            recipesList?.let {
                recipesListAdapter.submitList(it)
            }
        }
    }

    private fun openRecipe(recipe: Recipe) {
        findNavController().navigate(
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(
                recipe
            )
        )
    }

}