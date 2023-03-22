package com.example.smartlab.model.dto

data class CatalogItem(
    val id: String,
    val name: String,
    val description: String,
    val price: String,
    val category: String,
    val time_result: String,
    val preparation: String,
    val bio: String
)