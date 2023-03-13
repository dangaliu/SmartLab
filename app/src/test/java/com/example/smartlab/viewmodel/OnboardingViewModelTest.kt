package com.example.smartlab.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.smartlab.R
import com.example.smartlab.app.SmartLabApplication
import com.example.smartlab.model.dto.OnboardingItem
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock

class OnboardingViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    val application = mock<SmartLabApplication>()

    @Test
    fun `correctly extracting elements from the queue`() {
        val viewModel = OnboardingViewModel(application)
        val firstElement = OnboardingItem(
            title = "Анализы",
            description = "Экспресс сбор и получение проб",
            imageRes = R.drawable.onboarding_01
        )

        val secondElement = OnboardingItem(
            title = "Уведомления",
            description = "Вы быстро узнаете о результатах",
            imageRes = R.drawable.onboarding_02
        )

        val thirdElement = OnboardingItem(
            title = "Мониторинг",
            description = "Наши врачи всегда наблюдают \n" +
                    "за вашими показателями здоровья",
            imageRes = R.drawable.onboarding_03
        )

        assertEquals(firstElement, viewModel.nextPage())
        assertEquals(secondElement, viewModel.nextPage())
        assertEquals(thirdElement, viewModel.nextPage())
    }

    @Test
    fun `should queue size decrease when nextPage() called`() {
        val viewModel = OnboardingViewModel(application)
        assertEquals(3, viewModel.onboardingItems.size)
        viewModel.nextPage()
        assertEquals(2, viewModel.onboardingItems.size)
        viewModel.nextPage()
        assertEquals(1, viewModel.onboardingItems.size)
        viewModel.nextPage()
        assertEquals(0, viewModel.onboardingItems.size)
    }

    @Test
    fun `should change button text when last page`() {
        val viewModel = OnboardingViewModel(application)
        viewModel.nextPage()
        viewModel.nextPage()
        viewModel.nextPage()
        assertEquals("Завершить", viewModel.buttonText.value)
    }
}