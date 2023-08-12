package com.example.newsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R.*
import com.example.newsapp.models.Category

class CategoryAdapter(val context: Context, private val listener : OnClickEvent) : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(DiffUtilCallBack()) {

    inner class CategoryViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val image : ImageView = itemView.findViewById(id.ivTopic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(parent.context).inflate(layout.layout_category, parent, false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val topic = getItem(position)
        val drawble = ContextCompat.getDrawable(context, topic.sports)
        holder.image.setImageDrawable(drawble)

        holder.itemView.setOnClickListener{
            listener.OnCategoryClick(topic)
        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Category>(){
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.topicText == newItem.topicText
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

    }
}

interface OnClickEvent{
    fun OnCategoryClick(category: Category)
}