package com.example.smartlab.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.smartlab.model.api.requestModels.CreateProfileRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

object DataStore {
    val IS_ONBOARDING_PASSED = booleanPreferencesKey("isOnboardingPassed")
    val TOKEN = stringPreferencesKey("token")
    val EMAIL = stringPreferencesKey("email")
    val IS_CREATE_PATIENT_CARD_PASSED = booleanPreferencesKey("isCreatePatientCardPassed")

    val PATIENT_CARD = stringPreferencesKey("patientCard")

    suspend fun savePatientCard(
        context: Context,
        patientCard: CreateProfileRequest
    ) {
        context.dataStore.edit {
            val patientCardJson =
                Gson().toJson(patientCard, object : TypeToken<CreateProfileRequest>() {}.type)
            it[PATIENT_CARD] = patientCardJson
        }
    }

    fun getPatientCard(context: Context): Flow<CreateProfileRequest?> {
        val patientCardJsonFlow = context.dataStore.data.map {
            Gson().fromJson(it[PATIENT_CARD] ?: "{}", CreateProfileRequest::class.java)
        }
        return patientCardJsonFlow
    }


    suspend fun saveToken(context: Context, token: String): Flow<SaveStatus> {
        return flow {
            context.dataStore.edit {
                it[TOKEN] = token
                emit(SaveStatus.SUCCESS)
            }
        }

    }

    fun getToken(context: Context): Flow<String> {
        val token = context.dataStore.data.map {
            it[TOKEN] ?: ""
        }
        return token
    }

    suspend fun saveEmail(context: Context, email: String): Flow<SaveStatus> {
        return flow {
            context.dataStore.edit {
                it[EMAIL] = email
                emit(SaveStatus.SUCCESS)
            }
        }
    }

    fun getEmail(context: Context): Flow<String> {
        val email = context.dataStore.data.map {
            it[EMAIL] ?: ""
        }
        return email
    }

    suspend fun saveCreatePatientCardPassed(context: Context): Flow<SaveStatus> {
        return flow {
            context.dataStore.edit {
                it[IS_CREATE_PATIENT_CARD_PASSED] = true
                emit(SaveStatus.SUCCESS)
            }
        }
    }
}

enum class SaveStatus {
    SUCCESS, FAIL, NOTHING
}
