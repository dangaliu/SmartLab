package com.example.smartlab.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartlab.model.api.SmartLabClient
import com.example.smartlab.model.dto.NewsItem
import kotlinx.coroutines.launch

class AnalyzesViewModel: ViewModel() {

    private val _news: MutableLiveData<List<NewsItem>> = MutableLiveData()
    val news: LiveData<List<NewsItem>> = _news

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
}