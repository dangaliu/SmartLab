package com.example.smartlab.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smartlab.R
import com.example.smartlab.model.dto.OnboardingItem
import com.example.smartlab.utils.IS_ONBOARDING_PASSED
import com.example.smartlab.utils.dataStore
import kotlinx.coroutines.launch

class OnboardingViewModel(private val app: Application) : AndroidViewModel(app) {

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

    var buttonText: MutableLiveData<String> = MutableLiveData("Пропустить")

    var scrollCount = 0

    var isLastScreen = MutableLiveData<Boolean>()

    var isNavigatedToLoginScreen = false

    var onboardingPassedCallCount = 0

    fun setOnboardingPassed() {
        onboardingPassedCallCount++
        viewModelScope.launch {
            app.dataStore.edit { prefs ->
                prefs[IS_ONBOARDING_PASSED] = true
            }
        }
    }

    fun nextPage(): OnboardingItem {
        scrollCount++
        if (scrollCount == 2) buttonText.value = "Завершить"
        return onboardingItems.removeFirst()
    }

    fun navigateToLoginScreen() {
        setOnboardingPassed()
        isNavigatedToLoginScreen = true
    }
}