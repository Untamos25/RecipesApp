package com.example.recipesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemIngredientBinding
import entity.Ingredient
import kotlin.math.floor


class IngredientsAdapter(private val dataSet: List<Ingredient>) :
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
        val calculatedQuantity = ingredient.quantity.toDouble() * quantity

        with(viewHolder.binding) {
            tvIngredientName.text = ingredient.description
            tvIngredientQuantity.text = if (calculatedQuantity == floor(calculatedQuantity)) {
                "${calculatedQuantity.toInt()} ${ingredient.unitOfMeasure}"
            } else {
                "${String.format("%.1f", calculatedQuantity)} ${ingredient.unitOfMeasure}"
            }
        }
    }

    override fun getItemCount() = dataSet.size

    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }
}