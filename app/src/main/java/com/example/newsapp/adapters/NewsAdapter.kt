package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.fragments.BreakingNewsFragmentDirections
import com.example.newsapp.models.Article

class NewsAdapter : ListAdapter<Article, NewsAdapter.ArticleViewHolder>(DiffUtilCallback()){

    inner class ArticleViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val ivArticleImage: ImageView = itemView.findViewById(R.id.ivArticleImage)
        val title : TextView = itemView.findViewById(R.id.tvTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
       return ArticleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.article_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = currentList[position]
        Glide.with(holder.itemView.context).load(article.urlToImage).into(holder.ivArticleImage)
        holder.apply {
            title.text = article.title

            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(article) }
            }

            // Handling item clicks using interface
//            holder.itemView.setOnClickListener {
//              listener.onArticleClick(article)
//            }

        }
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
}

interface onClickItems{
    fun onArticleClick(article : Article)
}