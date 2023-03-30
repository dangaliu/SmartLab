package com.example.smartlab.model.dto

data class Order(
    val address: String,
    val date_time: String,
    val phone: String,
    val comment: String,
    val audio_comment: String,
    val patients: List<Patient>
)
