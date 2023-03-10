package com.example.smartlab.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.smartlab.model.dto.OnboardingItem
import com.example.smartlab.view.fragments.OnboardingItemFragment

//class OnboardingAdapter(
//    val items: MutableList<OnboardingItem>
//) : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {
//
//    inner class OnboardingViewHolder(val binding: ItemOnboardingBinding) :
//        RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
//        val binding =
//            ItemOnboardingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return OnboardingViewHolder(binding)
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
//
//    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
//        val item = items[position]
//        with(holder.binding) {
//            tvOnboardingTitle.text = item.title
//            tvOnboardingDescription.text = item.description
//            ivOnboardingImage.setImageResource(item.imageRes)
//        }
//    }
//}

class OnboardingAdapter(
    val fragmentActivity: FragmentActivity,
    val items: MutableList<OnboardingItem>
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return OnboardingItemFragment.newInstance(position)
    }


}