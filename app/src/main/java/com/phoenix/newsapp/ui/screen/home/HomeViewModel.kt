package com.phoenix.newsapp.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.phoenix.newsapp.data.model.Article
import com.phoenix.newsapp.data.network.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    fun getTopHeadlines(): Flow<PagingData<Article>> {
        return repository.getTopHeadlines()
            .cachedIn(viewModelScope)
    }
}