package com.example.smartlab.model.api

import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface SmartLabService {

    @POST("api/sendCode")
    suspend fun sendCode(@Header("email") email: String): Response<Unit>
}