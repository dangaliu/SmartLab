package com.example.smartlab.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlab.databinding.FragmentOnboardingBinding
import com.example.smartlab.utils.SwipeControlTouchListener
import com.example.smartlab.utils.SwipeDirection
import com.example.smartlab.view.adapters.OnboardingAdapter
import com.example.smartlab.viewmodel.OnboardingViewModel
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingBinding
    private val viewModel: OnboardingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOnboardingViewPager()
    }

    private fun initOnboardingViewPager() {
        binding.vpOnboarding.adapter = OnboardingAdapter(
            requireActivity(), viewModel.onboardingItems
        )
        (binding.vpOnboarding[0] as RecyclerView).addOnItemTouchListener(SwipeControlTouchListener().apply {
            setSwipeDirection(
                SwipeDirection.RIGHT
            )
        })
        TabLayoutMediator(binding.tabLayoutIndicator, binding.vpOnboarding) { _, _ -> }.attach()
        (binding.vpOnboarding[0] as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }
}