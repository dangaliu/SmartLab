package com.example.smartlab.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartlab.model.api.SmartLabClient
import com.example.smartlab.model.dto.AnalysisItem
import com.example.smartlab.model.dto.NewsItem
import kotlinx.coroutines.launch

class AnalyzesViewModel : ViewModel() {

    private val _news: MutableLiveData<List<NewsItem>> = MutableLiveData()
    val news: LiveData<List<NewsItem>> = _news

    private val _categories: MutableLiveData<HashSet<String>> = MutableLiveData()
    val categories: LiveData<HashSet<String>> = _categories

    private val _catalog: MutableLiveData<List<AnalysisItem>> = MutableLiveData()
    val catalog: LiveData<List<AnalysisItem>> = _catalog

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