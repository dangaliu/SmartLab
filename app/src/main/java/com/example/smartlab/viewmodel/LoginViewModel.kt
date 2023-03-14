package com.example.smartlab.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartlab.model.api.SmartLabClient
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val _sendCodeStatus: MutableLiveData<SendCodeStatus> = MutableLiveData()
    val sendCodeStatus: LiveData<SendCodeStatus> = _sendCodeStatus

    fun sendCode(email: String) {
        viewModelScope.launch {
            when(SmartLabClient.retrofit.sendCode(email).code()) {
                200 -> _sendCodeStatus.value = SendCodeStatus.SUCCESS
                422 -> _sendCodeStatus.value = SendCodeStatus.FAIL
            }
        }

    }
}

enum class SendCodeStatus {
    SUCCESS, FAIL
}