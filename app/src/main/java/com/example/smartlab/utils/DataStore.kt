package com.example.smartlab.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

object DataStore {
    val IS_ONBOARDING_PASSED = booleanPreferencesKey("isOnboardingPassed")
    val TOKEN = stringPreferencesKey("token")
    val EMAIL = stringPreferencesKey("email")

    suspend fun saveToken(context: Context, token: String): Flow<SaveStatus> {
        return flow {
            context.dataStore.edit {
                it[TOKEN] = token
                emit(SaveStatus.SUCCESS)
            }
        }

    }

    suspend fun getToken(context: Context): Flow<String> {
        return flow {
            context.dataStore.data.map {
                emit(it[TOKEN] ?: "")
            }
        }
    }

    suspend fun saveEmail(context: Context, email: String): Flow<SaveStatus> {
        return flow {
            context.dataStore.edit {
                it[EMAIL] = email
                emit(SaveStatus.SUCCESS)
            }
        }
    }

    suspend fun getEmail(context: Context): Flow<String> {
        return flow {
            context.dataStore.data.map {
                emit(it[EMAIL] ?: "")
            }
        }
    }
}

enum class SaveStatus {
    SUCCESS, FAIL
}
