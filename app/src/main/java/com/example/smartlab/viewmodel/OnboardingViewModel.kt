package com.example.smartlab.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartlab.R
import com.example.smartlab.model.dto.OnboardingItem

class OnboardingViewModel : ViewModel() {

    val onboardingItems = ArrayDeque(
        mutableListOf(
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
    )

    var scrollCount = 0

    var isLastScreen = MutableLiveData<Boolean>()
}