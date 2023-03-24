package com.example.smartlab.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smartlab.model.api.SmartLabClient
import com.example.smartlab.model.api.requestModels.CreateProfileRequest
import com.example.smartlab.model.api.responseModels.CreateProfileResponse
import com.example.smartlab.utils.DataStore
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ProfileViewModel(private val app: Application) : AndroidViewModel(app) {

    private val _patientCard: MutableLiveData<CreateProfileRequest?> = MutableLiveData()
    val patientCard: LiveData<CreateProfileRequest?> = _patientCard

    private val _createProfileStatus: MutableLiveData<CreateProfileResponse> = MutableLiveData()
    val createProfileStatus: LiveData<CreateProfileResponse> = _createProfileStatus

    private val TAG = this::class.java.simpleName

    private var token: String = ""

    init {
        viewModelScope.launch {
            DataStore.getToken(app).collect {
                token = it
            }
        }
    }

    var isEditMode = false

    fun getPatientCard() {
        viewModelScope.launch {
            DataStore.getPatientCard(app).collect {
                _patientCard.value = it
            }
        }
    }


    fun savePatientCard(patientCard: CreateProfileRequest) {
        viewModelScope.launch {
            DataStore.savePatientCard(app, patientCard)
        }
    }

    fun createProfile(profile: CreateProfileRequest) {
        viewModelScope.launch {
            val response = SmartLabClient.retrofit.createProfile("Bearer $token", profile)
            if (response.isSuccessful) {
                _createProfileStatus.value = response.body()
            }
        }
    }

    fun updateProfile(profile: CreateProfileRequest) {
        viewModelScope.launch {
            val response = SmartLabClient.retrofit.updateProfile("Bearer $token", profile)
            if (response.isSuccessful) {
                Log.d(TAG, "updateProfile: success")
            } else {
                Log.d(TAG, "updateProfile: error ${response.message()}")
            }
        }
    }

    fun updateAvatar(avatar: MultipartBody.Part) {
        viewModelScope.launch {
            val response = SmartLabClient.retrofit.updateAvatar("Bearer $token", avatar)
            Log.d(TAG, "updateAvatar: ${response.code()}")
        }
    }
}