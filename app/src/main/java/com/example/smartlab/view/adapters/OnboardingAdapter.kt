package com.example.smartlab.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlab.databinding.ItemOnboardingBinding
import com.example.smartlab.model.dto.OnboardingItem

class OnboardingAdapter(
    val items: MutableList<OnboardingItem>
) : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    inner class OnboardingViewHolder(val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val binding =
            ItemOnboardingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnboardingViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            tvOnboardingTitle.text = item.title
            tvOnboardingDescription.text = item.description
            ivOnboardingImage.setImageResource(item.imageRes)
        }
    }
}