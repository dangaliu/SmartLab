package com.example.smartlab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smartlab.model.api.SmartLabClient
import com.example.smartlab.model.dto.CatalogItem
import com.example.smartlab.model.dto.NewsItem
import com.example.smartlab.model.room.SmartlabDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AnalyzesViewModel(private val app: Application) : AndroidViewModel(app) {

    private val _news: MutableLiveData<List<NewsItem>> = MutableLiveData()
    val news: LiveData<List<NewsItem>> = _news

    private val _categories: MutableLiveData<HashSet<String>> = MutableLiveData()
    val categories: LiveData<HashSet<String>> = _categories

    private val db = SmartlabDatabase.getDb(app)

    private val _catalog: MutableLiveData<List<CatalogItem>> = MutableLiveData()
    val catalog: LiveData<List<CatalogItem>> = _catalog

    val dbCatalog: LiveData<List<CatalogItem>> = db.getDao().getAllAnalyzes()


    fun getNews() {
        viewModelScope.launch {
            val response = SmartLabClient.retrofit.getNews()
            if (response.isSuccessful) {
                if (response.body() != null) {
                    _news.value = response.body()
                }
            }
        }
    }

    fun fillDatabase(items: List<CatalogItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            items.forEach {
                db.getDao().addAnalyzeToCart(it)
            }
        }
    }

    fun updateCatalogItem(item: CatalogItem) {
        viewModelScope.launch(Dispatchers.IO) {
            db.getDao().updateAnalyze(item)
        }
    }

    fun getCatalog() {
        viewModelScope.launch {
            val response = SmartLabClient.retrofit.getCatalog()
            if (response.isSuccessful) {
                if (response.body() != null) {
                    _catalog.value = response.body()
                    val currentCategories = HashSet<String>()
                    response.body()!!.forEach {
                        currentCategories.add(it.category)
                    }
                    _categories.value = currentCategories
                }
            }
        }
    }
}