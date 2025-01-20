package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.RecipesListFragment.Companion.ARG_RECIPE
import com.example.recipesapp.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import entity.Ingredient
import entity.Recipe
import java.io.InputStream
import java.lang.IllegalStateException

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding для FragmentRecipeBinding не должен быть null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedINstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
        } else {
            arguments?.getParcelable(ARG_RECIPE)
        }

        recipe?.let { recipe ->
            initUI(recipe)
            initRecycler(recipe.ingredients, recipe.method)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler(ingredients: List<Ingredient>, method: List<String>) {
        val ingredientsAdapter = IngredientsAdapter(ingredients)
        val methodAdapter = MethodAdapter(method)

        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        divider.dividerColor = ContextCompat.getColor(requireContext(), R.color.divider)
        binding.rvIngredients.addItemDecoration(divider)
        binding.rvMethod.addItemDecoration(divider)

        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvMethod.adapter = methodAdapter
    }

    private fun initUI(recipe: Recipe) {
        binding.tvRecipeTitle.text = recipe.title

        recipe.imageUrl.let { imageUrl ->
            try {
                val inputStream: InputStream = requireContext().assets.open(imageUrl)
                val drawable = Drawable.createFromStream(inputStream, null)
                binding.imgRecipe.setImageDrawable(drawable)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}
