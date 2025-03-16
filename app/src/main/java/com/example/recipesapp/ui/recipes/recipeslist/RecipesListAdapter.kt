package com.example.recipesapp.ui.recipes.recipeslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ItemRecipeBinding
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.model.getFullImageUrl

class RecipesListAdapter(var dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }


    class ViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecipeBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val recipe = dataSet[position]

        with(viewHolder.binding) {
            tvTitle.text = recipe.title

            Glide.with(viewHolder.itemView.context)
                .load(recipe.getFullImageUrl())
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(imageCategory)

            imageCategory.contentDescription =
                viewHolder.itemView.context.getString(R.string.category_image, recipe.title)

            root.setOnClickListener { itemClickListener?.onItemClick(recipe.id) }
        }
    }

    override fun getItemCount() = dataSet.size

    fun submitList(newList: List<Recipe>) {
        dataSet = newList
        notifyDataSetChanged()
    }
}