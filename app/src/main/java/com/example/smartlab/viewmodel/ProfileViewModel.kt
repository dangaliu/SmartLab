package com.example.smartlab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smartlab.model.api.requestModels.CreateProfileRequest
import com.example.smartlab.utils.DataStore
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileViewModel(private val app: Application): AndroidViewModel(app) {

    private val _patientCard: MutableLiveData<CreateProfileRequest?> = MutableLiveData()
    val patientCard: LiveData<CreateProfileRequest?> = _patientCard

    fun getPatientCard() {
        viewModelScope.launch {
            DataStore.getPatientCard(app).collect {
                _patientCard.value = it
            }
        }
    }
}