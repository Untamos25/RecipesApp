package com.example.recipesapp.ui.recipes.recipeslist

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ItemRecipeBinding
import com.example.recipesapp.model.Recipe
import java.io.InputStream

class RecipesListAdapter(private val dataSet: List<Recipe>) :
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

            try {
                val context = viewHolder.itemView.context
                val inputStream: InputStream = context.assets.open(recipe.imageUrl)
                val drawable = Drawable.createFromStream(inputStream, null)
                imageCategory.setImageDrawable(drawable)
                imageCategory.contentDescription =
                    context.getString(R.string.category_image, recipe.title)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            root.setOnClickListener { itemClickListener?.onItemClick(recipe.id) }
        }
    }

    override fun getItemCount() = dataSet.size
}