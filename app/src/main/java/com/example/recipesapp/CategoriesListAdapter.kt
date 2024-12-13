package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemCategoryBinding
import entity.Category
import java.io.InputStream

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemCategoryBinding = ItemCategoryBinding.bind(view)

        val imageView: ImageView = binding.imageCategory
        val titleTextView: TextView = binding.tvTitle
        val descriptionTextView: TextView = binding.tvDescription
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_category, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val category = dataSet[position]
        viewHolder.titleTextView.text = category.title
        viewHolder.descriptionTextView.text = category.description

        try {
            val inputStream: InputStream =
                viewHolder.itemView.context.assets.open(category.imageUrl)
            val drawable = Drawable.createFromStream(inputStream, null)
            viewHolder.imageView.setImageDrawable(drawable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount() = dataSet.size

}