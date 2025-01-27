package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
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

const val MIN_AMOUNT_OF_PORTIONS = 1

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding для FragmentRecipeBinding не должен быть null")

    private var isFavorite = false

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

        val dividerMargin = resources.getDimensionPixelSize(R.dimen.space_three_quarters)
        val divider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
                dividerColor = ContextCompat.getColor(requireContext(), R.color.divider)
                isLastItemDecorated = false
                dividerInsetStart = dividerMargin
                dividerInsetEnd = dividerMargin
            }

        with(binding) {
            tvPortionsValue.text = "$MIN_AMOUNT_OF_PORTIONS"
            rvIngredients.addItemDecoration(divider)
            rvMethod.addItemDecoration(divider)

            sbPortions.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    tvPortionsValue.text = progress.toString()
                    ingredientsAdapter.updateIngredients(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }
            })

            rvIngredients.adapter = ingredientsAdapter
            rvMethod.adapter = methodAdapter
        }
    }

    private fun initUI(recipe: Recipe) {
        with (binding) {
            tvRecipeTitle.text = recipe.title
            ibFavorites.setImageResource(R.drawable.ic_heart_empty)

            ibFavorites.setOnClickListener {
                if (!isFavorite) ibFavorites.setImageResource(R.drawable.ic_heart)
                else ibFavorites.setImageResource(R.drawable.ic_heart_empty)
                isFavorite = !isFavorite
            }
        }

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
