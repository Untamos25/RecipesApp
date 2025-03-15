package com.example.recipesapp.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ItemCategoryBinding
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.getFullImageUrl

class CategoriesListAdapter(var dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

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

            Glide.with(viewHolder.itemView.context)
                .load(category.getFullImageUrl())
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(imageCategory)

            imageCategory.contentDescription =
                viewHolder.itemView.context.getString(R.string.category_image, category.title)

            root.setOnClickListener { itemClickListener?.onItemClick(category.id) }
        }
    }

    override fun getItemCount() = dataSet.size

    fun submitList(newList: List<Category>) {
        dataSet = newList
        notifyDataSetChanged()
    }
}