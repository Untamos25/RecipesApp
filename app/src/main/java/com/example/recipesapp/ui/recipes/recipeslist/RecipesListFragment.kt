package com.example.recipesapp.ui.recipes.recipeslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.databinding.FragmentListRecipesBinding
import java.lang.IllegalStateException

class RecipesListFragment : Fragment() {

    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding для FragmentListRecipesBinding не должен быть null")

    private val viewModel: RecipesListViewModel by viewModels()
    private val recipesListFragmentArgs: RecipesListFragmentArgs by navArgs()
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

        val categoryId = recipesListFragmentArgs.category.id

        initRecycler()

        viewModel.loadRecipesList(categoryId)
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
        findNavController().navigate(
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(
                recipeId
            )
        )
    }

}