package com.example.smartlab.model.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "analyzes")
data class CatalogItem(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String,
    val price: String,
    val category: String,
    val time_result: String,
    val preparation: String,
    val bio: String,
    var isInCard: Boolean = false,
    var totalPrice: Int = 0,
    var count: Int = 0,
    var patientCount: Int = 1
)