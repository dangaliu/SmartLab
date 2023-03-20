package com.example.smartlab.model.api

import com.example.smartlab.model.api.requestModels.CreateProfileRequest
import com.example.smartlab.model.api.responseModels.CreateProfileResponse
import com.example.smartlab.model.api.responseModels.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface SmartLabService {

    @POST("api/sendCode")
    suspend fun sendCode(@Header("email") email: String): Response<Unit>

    @POST("api/signin")
    suspend fun signIn(
        @Header("email") email: String,
        @Header("code") code: String
    ): Response<TokenResponse>

    @POST("api/createProfile")
    suspend fun createProfile(
        @Header("Authorization") token: String,
        @Body profile: CreateProfileRequest
    ): Response<CreateProfileResponse>
}