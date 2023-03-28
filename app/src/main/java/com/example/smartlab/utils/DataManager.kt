package com.example.smartlab.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.smartlab.model.api.requestModels.CreateProfileRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

object DataManager {
    val IS_ONBOARDING_PASSED = booleanPreferencesKey("isOnboardingPassed")
    val TOKEN = stringPreferencesKey("token")
    val EMAIL = stringPreferencesKey("email")
    val IS_CREATE_PATIENT_CARD_PASSED = booleanPreferencesKey("isCreatePatientCardPassed")
    val AVATAR_URI = stringPreferencesKey("avatarUri")

    val PATIENT_CARD = stringPreferencesKey("patientCard")

    private lateinit var masterKey: MasterKey
    private lateinit var sharedPreferences: EncryptedSharedPreferences

    fun initEncryptedSharedPrefs(context: Context) {
        masterKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "smartLabPrefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
    }

    fun encryptToken(token: String) {
        sharedPreferences.edit().putString("token", token).apply()
    }

    fun encryptPassword(password: String) {
        sharedPreferences.edit().putString("password", password).apply()
    }

    fun decryptToken(): String {
        return sharedPreferences.getString("token", "") ?: ""
    }

    fun decryptPassword(): String {
        return sharedPreferences.getString("password", "") ?: ""
    }

    suspend fun saveAvatarUri(
        context: Context,
        avatarUri: String
    ) {
        context.dataStore.edit {
            it[AVATAR_URI] = avatarUri
        }
    }

    fun getAvatarUri(context: Context): Flow<String> {
        val uri = context.dataStore.data.map {
            it[AVATAR_URI] ?: ""
        }
        return uri
    }

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
