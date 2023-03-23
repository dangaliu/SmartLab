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

class CreatePatientCardViewModel(private val app: Application) : AndroidViewModel(app) {

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


    fun createProfile(profile: CreateProfileRequest) {
        viewModelScope.launch {
            val response = SmartLabClient.retrofit.createProfile("Bearer $token", profile)
            if (response.isSuccessful) {
                _createProfileStatus.value = response.body()
            }
        }
    }

    fun setCreatePatientCardPassed() {
        viewModelScope.launch {
            DataStore.saveCreatePatientCardPassed(app).collect {
                Log.d(TAG, "setCreatePatientCardPassed: $it")
            }
        }
    }

    fun savePatientCard(patientCard: CreateProfileRequest) {
        viewModelScope.launch {
            DataStore.savePatientCard(app, patientCard)
        }
    }
}