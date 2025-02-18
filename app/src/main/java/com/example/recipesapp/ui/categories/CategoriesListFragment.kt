package com.example.recipesapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.recipesapp.R
import com.example.recipesapp.UiConstants.ARG_CATEGORY_ID
import com.example.recipesapp.UiConstants.ARG_CATEGORY_IMAGE_URL
import com.example.recipesapp.UiConstants.ARG_CATEGORY_NAME
import com.example.recipesapp.databinding.FragmentListCategoriesBinding
import com.example.recipesapp.ui.recipes.recipeslist.RecipesListFragment

class CategoriesListFragment : Fragment() {

    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding для FragmentListCategoriesBinding не должен быть null")

    private val viewModel: CategoriesListViewModel by viewModels()

    private lateinit var categoriesListAdapter: CategoriesListAdapter

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

        categoriesListAdapter = CategoriesListAdapter(listOf())

        initRecycler()

        viewModel.loadCategories()
        viewModel.categoriesListState.observe(viewLifecycleOwner) { state ->
            initUI(state)
        }

        viewModel.navigateToRecipesList.observe(viewLifecycleOwner) { navData ->
            openRecipesList(
                navData.categoryId,
                navData.categoryName,
                navData.categoryImageUrl
            )
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

    private fun openRecipesList(
        categoryId: Int,
        categoryName: String,
        categoryImageUrl: String
    ) {
        val bundle = bundleOf(
            ARG_CATEGORY_ID to categoryId,
            ARG_CATEGORY_NAME to categoryName,
            ARG_CATEGORY_IMAGE_URL to categoryImageUrl
        )

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipesListFragment>(R.id.mainContainer, args = bundle)
        }
    }

}