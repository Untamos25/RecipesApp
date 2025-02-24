package com.example.recipesapp.ui.recipes.recipeslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.R
import com.example.recipesapp.UiConstants.ARG_CATEGORY_ID
import com.example.recipesapp.UiConstants.ARG_RECIPE_ID
import com.example.recipesapp.databinding.FragmentListRecipesBinding
import java.lang.IllegalStateException

class RecipesListFragment : Fragment() {

    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding для FragmentListRecipesBinding не должен быть null")

    private val viewModel: RecipesListViewModel by viewModels()
    private val recipesListAdapter = RecipesListAdapter(listOf())

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

        val categoryId = arguments?.getInt(ARG_CATEGORY_ID)

        initRecycler()

        categoryId?.let {
            viewModel.loadRecipesList(categoryId)
        }

        viewModel.recipesListState.observe(viewLifecycleOwner) { state ->
            initUI(state)
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
                    openRecipeByRecipeId(recipeId)
                }
            })
    }

    private fun initUI(state: RecipesListViewModel.RecipesListState) {
        val category = state.category
        val recipesList = state.recipesList

        with(binding) {
            category?.let {
                tvCategory.text = it.title
                imgCategory.setImageDrawable(state.categoryImage)
            }
            recipesList?.let {
                recipesListAdapter.submitList(it)
            }
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = bundleOf(
            ARG_RECIPE_ID to recipeId
        )

        findNavController().navigate(R.id.action_recipesListFragment_to_recipeFragment, bundle)
    }

}