package com.example.smartlab.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.smartlab.R
import com.example.smartlab.app.SmartLabApplication
import com.example.smartlab.model.dto.OnboardingItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.Mockito
import org.mockito.kotlin.mock

class OnboardingViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    class MainDispatcherRule (
        val dispatcher: TestDispatcher = StandardTestDispatcher()
    ): TestWatcher() {
        override fun starting(description: Description) {
            Dispatchers.setMain(dispatcher)
        }

        override fun finished(description: Description) {
            Dispatchers.resetMain()
        }
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    val application = mock<SmartLabApplication>()
    lateinit var viewModel: OnboardingViewModel


    @Before
    fun before() {
        viewModel = OnboardingViewModel(application)
    }

    @Test
    fun `correctly extracting elements from the queue`() {
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
        viewModel.nextPage()
        viewModel.nextPage()
        viewModel.nextPage()
        assertEquals("Завершить", viewModel.buttonText.value)
    }

    @Test
    fun `should navigated to login screen when clicked navigate button`() {
        viewModel.navigateToLoginScreen()
        assertEquals(viewModel.isNavigatedToLoginScreen, true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should call onboarding passed`() = runTest {
        viewModel.navigateToLoginScreen()
        assertEquals(viewModel.onboardingPassedCallCount, 1)
    }
}