package com.example.smartlab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.smartlab.model.room.SmartlabDatabase

class CartViewModel(private val app: Application) : AndroidViewModel(app) {

    private val db = SmartlabDatabase.getDb(app)
    val items = db.getDao().getAllAnalyzes()
}