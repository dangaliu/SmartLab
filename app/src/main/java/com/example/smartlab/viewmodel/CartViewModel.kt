package com.example.smartlab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartlab.model.dto.CatalogItem
import com.example.smartlab.model.room.SmartlabDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel(private val app: Application) : AndroidViewModel(app) {

    private val db = SmartlabDatabase.getDb(app)
    val items = db.getDao().getAllAnalyzes()

    var cartItems = listOf<CatalogItem>()

    fun clearAll() {
        viewModelScope.launch(Dispatchers.IO) {
            cartItems.forEach {
                db.getDao().updateAnalyze(it.copy(isInCard = false))
            }
        }
    }

    fun onPlusClick(item: CatalogItem) {
        viewModelScope.launch(Dispatchers.IO) {
            db.getDao().updateAnalyze(item.copy(patientCount = item.patientCount + 1))
        }
    }

    fun onMinusClick(item: CatalogItem) {
        viewModelScope.launch(Dispatchers.IO) {
            db.getDao().updateAnalyze(item.copy(patientCount = item.patientCount - 1))
        }
    }

    fun deleteFromCart(item: CatalogItem) {
        viewModelScope.launch(Dispatchers.IO) {
            db.getDao().updateAnalyze(item.copy(isInCard = false))
        }
    }
}