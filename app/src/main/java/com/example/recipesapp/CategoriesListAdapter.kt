package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemCategoryBinding
import entity.Category
import java.io.InputStream

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val category = dataSet[position]
        with(viewHolder.binding) {
            tvTitle.text = category.title
            tvDescription.text = category.description
            try {
                val inputStream: InputStream = viewHolder.itemView.context.assets.open(category.imageUrl)
                val drawable = Drawable.createFromStream(inputStream, null)
                imageCategory.setImageDrawable(drawable)
                imageCategory.contentDescription = "Изображение категории ${category.title}"
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getItemCount() = dataSet.size

}