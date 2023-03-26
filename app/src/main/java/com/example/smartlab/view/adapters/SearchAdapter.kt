package com.example.smartlab.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlab.databinding.ItemSearchBinding
import com.example.smartlab.model.dto.CatalogItem

class SearchAdapter(
    private val context: Context,
    var items: List<CatalogItem>,
    var onItemClickListener: (CatalogItem) -> Unit = {}
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding =
            ItemSearchBinding.inflate(LayoutInflater.from(context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            tvTitle.text = item.name
            tvTimeResult.text = item.time_result
            tvPrice.text = "${item.price} ₽"
            root.setOnClickListener {
                onItemClickListener(item)
            }
        }
    }

    fun updateItems(catalog: List<CatalogItem>) {
        this.items = catalog
        notifyDataSetChanged()
    }
}