package com.example.recipesapp.ui.recipes.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.R
import com.example.recipesapp.UiConstants.ARG_RECIPE_ID
import com.example.recipesapp.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import java.lang.IllegalStateException

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding для FragmentRecipeBinding не должен быть null")

    private val viewModel: RecipeViewModel by viewModels()

    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var methodAdapter: MethodAdapter
    private lateinit var divider: MaterialDividerItemDecoration


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeId = arguments?.getInt(ARG_RECIPE_ID)

        ingredientsAdapter = IngredientsAdapter(mutableListOf())
        methodAdapter = MethodAdapter(mutableListOf())
        divider = createDivider()
        setupSeekBar()

        initRecycler()

        recipeId?.let {
            viewModel.loadRecipe(recipeId)
        }

        viewModel.recipeState.observe(viewLifecycleOwner) { state ->
            initUI(state)
            state.recipe?.let { recipe ->
                ingredientsAdapter.submitList(recipe.ingredients)
                methodAdapter.submitList(recipe.method)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        with(binding) {
            rvIngredients.layoutManager = LinearLayoutManager(requireContext())
            rvIngredients.adapter = ingredientsAdapter
            rvIngredients.addItemDecoration(divider)

            rvMethod.layoutManager = LinearLayoutManager(requireContext())
            rvMethod.adapter = methodAdapter
            rvMethod.addItemDecoration(divider)
        }
    }

    private fun initUI(state: RecipeViewModel.RecipeState) {
        val recipe = state.recipe
        val isFavorite = state.isFavorite

        updateFavoriteIcon(isFavorite)

        with(binding) {
            recipe?.let {
                tvRecipeTitle.text = it.title

                ibFavorites.setOnClickListener {
                    viewModel.onFavoritesClicked()
                }

                sbPortions.progress = state.numberOfPortions
                tvPortionsValue.text = state.numberOfPortions.toString()

                imgRecipe.setImageDrawable(state.recipeImage)
            }
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        binding.ibFavorites.apply {
            setImageResource(if (isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_empty)
        }
    }

    private fun createDivider(): MaterialDividerItemDecoration {
        val dividerMargin = resources.getDimensionPixelSize(R.dimen.space_three_quarters)
        val divider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
                dividerColor = ContextCompat.getColor(requireContext(), R.color.divider)
                isLastItemDecorated = false
                dividerInsetStart = dividerMargin
                dividerInsetEnd = dividerMargin
            }
        return divider
    }

    private fun setupSeekBar() {
        with(binding) {
            sbPortions.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    tvPortionsValue.text = progress.toString()
                    ingredientsAdapter.updateQuantity(progress)
                    viewModel.updateNumberOfPortions(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }
}