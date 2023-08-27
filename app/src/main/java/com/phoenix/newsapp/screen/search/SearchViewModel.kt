package com.phoenix.newsapp.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.phoenix.newsapp.data.model.Article
import com.phoenix.newsapp.data.Repository
import com.phoenix.newsapp.screen.destinations.BrowserScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository,
): ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()
    var searchedArticles = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    private lateinit var navigator: DestinationsNavigator

    fun updateNavigator(navigator: DestinationsNavigator) {
        this.navigator = navigator
    }

    fun updateListState(index: Int) {
        _uiState.update { it.copy(firstVisibleItem = index) }
    }

    private fun searchArticles() {
        if (uiState.value.searchedQuery == "") return
        _uiState.update { it.copy(isSearched = true) }
        viewModelScope.launch {
            repository.getEverything(uiState.value.searchedQuery)
                .cachedIn(viewModelScope).collect{
                    searchedArticles.value = it
                }
        }
    }

    fun onEvent(event: SearchUiEvent) {
        when(event) {
            SearchUiEvent.GoToHomeScreen ->
                navigator.navigateUp()
            SearchUiEvent.OnHitSearch ->
                searchArticles()
            is SearchUiEvent.OnSearchQueryChange ->
                _uiState.update { it.copy(searchedQuery = event.query) }

            is SearchUiEvent.OnClickItem ->
                navigator.navigate(BrowserScreenDestination(event.article))
        }
    }

}