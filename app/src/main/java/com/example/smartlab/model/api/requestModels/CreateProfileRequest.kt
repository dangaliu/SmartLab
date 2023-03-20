package com.example.smartlab.model.api.requestModels

data class CreateProfileRequest(
    val firstname: String,
    val lastname: String,
    val middlename: String,
    val bith: String,
    val pol: String,
    val image: String
)
