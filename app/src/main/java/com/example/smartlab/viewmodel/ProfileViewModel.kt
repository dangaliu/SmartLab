package com.example.smartlab.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
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

    private val _createdProfile: MutableLiveData<CreateProfileResponse> = MutableLiveData()
    val createdProfile: LiveData<CreateProfileResponse> = _createdProfile

    private val _updatedProfile: MutableLiveData<CreateProfileResponse> = MutableLiveData()
    val updatedProfile: LiveData<CreateProfileResponse> = _updatedProfile

    private val _avatarUri: MutableLiveData<Uri> = MutableLiveData()
    val avatarUri: LiveData<Uri> = _avatarUri

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

    fun saveAvatarUri(avatarUri: Uri) {
        viewModelScope.launch {
            DataStore.saveAvatarUri(app, avatarUri.toString())
        }
    }

    fun getAvatarUri() {
        viewModelScope.launch {
            DataStore.getAvatarUri(app).collect {
                _avatarUri.value = it.toUri()
            }
        }
    }

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
                _createdProfile.value = response.body()
            }
        }
    }

    fun updateProfile(profile: CreateProfileRequest) {
        viewModelScope.launch {
            val response = SmartLabClient.retrofit.updateProfile("Bearer $token", profile)
            if (response.isSuccessful) {
                Log.d(TAG, "updateProfile: success")
                if (response.body() != null) {
                    _updatedProfile.value = response.body()!!
                    Log.d(TAG, "updateProfile: ${response.body()!!}")
                }
            } else {
                Log.d(TAG, "updateProfile: error ${response.message()}")
            }
        }
    }

    fun updateAvatar(avatar: MultipartBody.Part) {
        viewModelScope.launch {
            val response = SmartLabClient.retrofit.updateAvatar("Bearer $token", avatar)
            if (response.isSuccessful) {
                _patientCard.value?.let {
                    updateProfile(it)
                }
            }
            Log.d(TAG, "updateAvatar: ${response.code()}")
        }
    }
}