package com.example.smartlab.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.Flow

object DataStore {
    val IS_ONBOARDING_PASSED = booleanPreferencesKey("isOnboardingPassed")
}
