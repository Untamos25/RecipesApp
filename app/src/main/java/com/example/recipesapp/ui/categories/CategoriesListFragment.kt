package com.example.recipesapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.databinding.FragmentListCategoriesBinding
import com.example.recipesapp.model.Category
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesListFragment : Fragment() {

    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding для FragmentListCategoriesBinding не должен быть null")

    private val viewModel: CategoriesListViewModel by viewModels()

    private val categoriesListAdapter = CategoriesListAdapter(listOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        viewModel.loadCategories()
        viewModel.categoriesListState.observe(viewLifecycleOwner) { state ->
            initUI(state)

            state.toastMessage?.let { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }

            if (state.openRecipeList && state.selectedCategory != null) {
                openRecipesList(state.selectedCategory)
                viewModel.onRecipeListOpened()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        binding.rvCategories.adapter = categoriesListAdapter

        categoriesListAdapter.setOnItemClickListener(
            object : CategoriesListAdapter.OnItemClickListener {
                override fun onItemClick(categoryId: Int) {
                    viewModel.onCategoryClicked(categoryId)
                }
            })
    }

    private fun initUI(state: CategoriesListViewModel.CategoriesListState) {
        val categoriesList = state.categoriesList

        categoriesList?.let {
            categoriesListAdapter.submitList(it)
        }
    }

    private fun openRecipesList(category: Category) {
        findNavController().navigate(
            CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(
                category
            )
        )
    }

}