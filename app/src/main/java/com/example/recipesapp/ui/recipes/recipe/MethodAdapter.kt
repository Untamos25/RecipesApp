package com.example.recipesapp.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ItemMethodBinding

class MethodAdapter(var dataSet: MutableList<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemMethodBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMethodBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val method = dataSet[position]
        val stepNumber = position + 1
        with(viewHolder.binding) {
            tvmMethodStep.text = root.context.getString(R.string.method_step_format, stepNumber, method)
        }
    }

    override fun getItemCount() = dataSet.size

    fun submitList(newList: List<String>) {
        dataSet = newList.toMutableList()
        notifyDataSetChanged()
    }
}