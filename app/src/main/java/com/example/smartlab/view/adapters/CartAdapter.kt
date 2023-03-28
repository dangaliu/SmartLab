package com.example.smartlab.view.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlab.R
import com.example.smartlab.databinding.ItemCartBinding
import com.example.smartlab.model.dto.CatalogItem

class CartAdapter(
    private val context: Context,
    var items: List<CatalogItem>,
    val onMinusClickListener: (CatalogItem) -> Unit = {},
    val onPlusClickListener: (CatalogItem) -> Unit = {},
    val onDeleteClickListener: (CatalogItem) -> Unit = {}
) : RecyclerView.Adapter<CartAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding =
            ItemCartBinding.inflate(LayoutInflater.from(context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            tvTitle.text = item.name
            tvPrice.text = "${item.price} ₽"
            tvPatientCount.text = showPatientCount(item.patientCount)
            ivDelete.setOnClickListener {
                onDeleteClickListener(item)
            }
            if (item.patientCount == 1) {
                ivMinus.imageTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.minus_inactive))
            } else {
                ivMinus.imageTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.minus_active))
            }
            ivMinus.setOnClickListener {
                if (item.patientCount > 1) {
                    onMinusClickListener(item)
                }
            }
            ivPlus.setOnClickListener {
                onPlusClickListener(item)
            }
        }
    }

    private fun showPatientCount(count: Int): String {
        return if ((count == 1 || count % 10 == 1) && count != 11) {
            "$count пациент"
        } else if ((count % 10 > 1) && (count % 10 < 5) && (count % 100 != 12 && count % 100 != 13 && count % 100 != 14)) {
            "$count пациента"
        } else {
            "$count пациентов"
        }
    }

    fun updateItems(items: List<CatalogItem>) {
        this.items = items
        notifyDataSetChanged()
    }
}