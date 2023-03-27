package com.example.smartlab.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlab.databinding.ItemCatalogBinding
import com.example.smartlab.model.dto.CatalogItem

class CatalogAdapter(
    private val context: Context,
    var catalog: List<CatalogItem>,
    var onAddButtonClickListener: (CatalogItem) -> Unit = {},
    val onCardClickListener: (CatalogItem) -> Unit = {}
) : RecyclerView.Adapter<CatalogAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(val binding: ItemCatalogBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding =
            ItemCatalogBinding.inflate(LayoutInflater.from(context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return catalog.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = catalog[position]
        with(holder.binding) {
            tvName.text = item.name
            tvTimeResult.text = item.time_result
            tvPrice.text = "${item.price} ₽"
            btnAdd.setOnClickListener {
                onAddButtonClickListener(item)
            }
            root.setOnClickListener {
                onCardClickListener(item)
            }
        }
    }

    fun updateItems(catalog: List<CatalogItem>) {
        this.catalog = catalog
        notifyDataSetChanged()
    }
}