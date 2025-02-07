package com.example.recipesapp.ui.recipes.recipeslist

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipesapp.R
import com.example.recipesapp.UiConstants.ARG_CATEGORY_ID
import com.example.recipesapp.UiConstants.ARG_CATEGORY_IMAGE_URL
import com.example.recipesapp.UiConstants.ARG_CATEGORY_NAME
import com.example.recipesapp.UiConstants.ARG_RECIPE_ID
import com.example.recipesapp.data.STUB
import com.example.recipesapp.databinding.FragmentListRecipesBinding
import com.example.recipesapp.ui.recipes.recipe.RecipeFragment
import java.io.InputStream
import java.lang.IllegalStateException

class RecipesListFragment : Fragment() {

    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding для FragmentListRecipesBinding не должен быть null")

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

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

        categoryId = arguments?.getInt(ARG_CATEGORY_ID)
        categoryName = arguments?.getString(ARG_CATEGORY_NAME)
        categoryImageUrl = arguments?.getString(ARG_CATEGORY_IMAGE_URL)

        binding.tvCategory.text = categoryName
        categoryImageUrl?.let { imageUrl ->
            try {
                val inputStream: InputStream = requireContext().assets.open(imageUrl)
                val drawable = Drawable.createFromStream(inputStream, null)
                binding.imgCategory.setImageDrawable(drawable)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        categoryId?.let { initRecycler(it) }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler(recipeId: Int) {
        val recipesAdapter = RecipesListAdapter(STUB.getRecipesByCategoryId(recipeId))
        binding.rvRecipes.adapter = recipesAdapter

        recipesAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {

            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = bundleOf(
            ARG_RECIPE_ID to recipeId
        )

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
        }
    }

}
