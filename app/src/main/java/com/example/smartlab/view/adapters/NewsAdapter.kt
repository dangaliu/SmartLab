package com.example.smartlab.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartlab.databinding.ItemNewsBinding
import com.example.smartlab.model.dto.NewsItem
import com.example.smartlab.utils.dpToPx

class NewsAdapter(
    private val context: Context,
    var news: List<NewsItem>
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding =
            ItemNewsBinding.inflate(LayoutInflater.from(context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return news.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = news[position]
        with(holder.binding) {
            tvName.text = item.name
            tvDescription.text = item.description
            tvPrice.text = "${item.price} ₽"
            Glide.with(context).load(item.image).into(ivNews)
        }
        if (position == news.lastIndex) {
            holder.binding.newsRoot.updateLayoutParams<RecyclerView.LayoutParams> {
                setMargins(0, 0, 0, 0)
            }
        } else {
            holder.binding.newsRoot.updateLayoutParams<RecyclerView.LayoutParams> {
                setMargins(0, 0, context.dpToPx(16), 0)
            }
        }
    }

    fun updateItems(news: List<NewsItem>) {
        this.news = news
        notifyDataSetChanged()
    }
}