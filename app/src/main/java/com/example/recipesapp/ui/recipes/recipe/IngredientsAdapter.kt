package com.example.recipesapp.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.ModelConstants.MIN_AMOUNT_OF_PORTIONS
import com.example.recipesapp.databinding.ItemIngredientBinding
import com.example.recipesapp.model.Ingredient
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter(var dataSet: MutableList<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var quantity = MIN_AMOUNT_OF_PORTIONS

    class ViewHolder(val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemIngredientBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val ingredient = dataSet[position]

        val quantityBigDecimal = ingredient.quantity.toBigDecimalOrNull()

        with(viewHolder.binding) {
            tvIngredientName.text = ingredient.description

            tvIngredientQuantity.text = quantityBigDecimal?.let { quantityBigDecimal ->
                (quantityBigDecimal * BigDecimal(quantity))
                    .setScale(1, RoundingMode.HALF_UP)
                    .stripTrailingZeros()
                    .toPlainString() + " ${ingredient.unitOfMeasure}"
            } ?: ingredient.quantity

        }
    }

    override fun getItemCount() = dataSet.size

    fun updateQuantity(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }

    fun submitList(newList: List<Ingredient>) {
        dataSet = newList.toMutableList()
        notifyDataSetChanged()
    }
}