package com.example.recipesapp.ui.recipes.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentRecipeBinding
import com.example.recipesapp.model.getFullImageUrl
import com.google.android.material.divider.MaterialDividerItemDecoration
import java.lang.IllegalStateException

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding для FragmentRecipeBinding не должен быть null")

    private val viewModel: RecipeViewModel by viewModels()
    private val recipeFragmentArgs: RecipeFragmentArgs by navArgs()
    private val ingredientsAdapter = IngredientsAdapter(mutableListOf())
    private val methodAdapter = MethodAdapter(mutableListOf())

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

        val recipeId = recipeFragmentArgs.recipeId

        divider = createDivider()
        setupSeekBar()
        initRecycler()

        viewModel.loadRecipe(recipeId)
        viewModel.recipeState.observe(viewLifecycleOwner) { state ->
            initUI(state)
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
        val portionsCount = state.numberOfPortions

        updateFavoriteIcon(isFavorite)

        with(binding) {
            recipe?.let {
                tvRecipeTitle.text = it.title

                ibFavorites.setOnClickListener {
                    viewModel.onFavoritesClicked()
                }

                sbPortions.progress = state.numberOfPortions
                tvPortionsValue.text = state.numberOfPortions.toString()
                ingredientsAdapter.updateQuantity(portionsCount)
                ingredientsAdapter.submitList(recipe.ingredients)
                methodAdapter.submitList(recipe.method)

                Glide.with(this@RecipeFragment)
                    .load(recipe.getFullImageUrl())
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_error)
                    .into(imgRecipe)
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
        binding.sbPortions.setOnSeekBarChangeListener(
            PortionSeekBarListener { progress ->
                binding.tvPortionsValue.text = progress.toString()
                viewModel.updateNumberOfPortions(progress)
            }
        )
    }

    class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) :
        SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(
            seekBar: SeekBar?,
            progress: Int,
            fromUser: Boolean
        ) {
            onChangeIngredients(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }

}