package com.phoenix.newsapp.ui.screen.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.phoenix.newsapp.data.model.Article
import com.phoenix.newsapp.data.network.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    var searchQuery by mutableStateOf("")
    var searchedArticles = MutableStateFlow<PagingData<Article>>(PagingData.empty())

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun searchArticles(query: String) {
        viewModelScope.launch {
            repository.getEverything(query)
                .cachedIn(viewModelScope).collect{
                    searchedArticles.value = it
                }
        }
    }

}