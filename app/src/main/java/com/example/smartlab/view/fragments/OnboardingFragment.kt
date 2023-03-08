package com.example.smartlab.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smartlab.R
import com.example.smartlab.databinding.FragmentOnboardingBinding
import com.example.smartlab.model.dto.OnboardingItem
import com.example.smartlab.view.adapters.OnboardingAdapter

class OnboardingFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingBinding

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
        val onboardingItems = mutableListOf(
            OnboardingItem(
                title = "Анализы",
                description = "Экспресс сбор и получение проб",
                imageRes = R.drawable.onboarding_01
            ),
            OnboardingItem(
                title = "Уведомления",
                description = "Вы быстро узнаете о результатах",
                imageRes = R.drawable.onboarding_02
            ),
            OnboardingItem(
                title = "Мониторинг",
                description = "Наши врачи всегда наблюдают \n" +
                        "за вашими показателями здоровья",
                imageRes = R.drawable.onboarding_03
            )
        )
        binding.vpOnboarding.adapter = OnboardingAdapter(onboardingItems)
        binding.circleIndicator.setViewPager(binding.vpOnboarding)
    }
}