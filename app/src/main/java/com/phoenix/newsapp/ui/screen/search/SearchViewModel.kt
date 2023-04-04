package com.phoenix.newsapp.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.phoenix.newsapp.data.database.ArticleBookmarkDao
import com.phoenix.newsapp.data.model.Article
import com.phoenix.newsapp.data.Repository
import com.phoenix.newsapp.ui.bottomsheet.news.NewsSheetUiState
import com.phoenix.newsapp.ui.bottomsheet.news.SavedState
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
    private val articleBookmarkDao: ArticleBookmarkDao
): ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    private val _newsSheetUiState = MutableStateFlow(NewsSheetUiState())
    val uiState = _uiState.asStateFlow()
    val newsSheetUiState = _newsSheetUiState.asStateFlow()
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
                navigator.popBackStack()
            SearchUiEvent.OnHitSearch ->
                searchArticles()
            is SearchUiEvent.OnSearchQueryChange ->
                _uiState.update { it.copy(searchedQuery = event.query) }
        }
    }

    fun insertArticle(article: Article) {
        _newsSheetUiState.update { it.copy(savedState = SavedState.Loading) }
        viewModelScope.launch {
            articleBookmarkDao.insertArticle(article)
            _newsSheetUiState.update { it.copy(savedState = SavedState.Saved) }
        }
    }

    fun deleteArticle(article: Article) {
        _newsSheetUiState.update { it.copy(savedState = SavedState.Loading) }
        viewModelScope.launch {
            articleBookmarkDao.deleteArticle(article)
            _newsSheetUiState.update { it.copy(savedState = SavedState.NotSaved) }
        }
    }

    suspend fun isArticleExistsInFavorite(url: String) {
        _newsSheetUiState.update { it.copy(savedState = SavedState.Loading) }
        val isExist = articleBookmarkDao.isRowExist(url)
        if (isExist)
            _newsSheetUiState.update { it.copy(savedState = SavedState.Saved) }
        else
            _newsSheetUiState.update { it.copy(savedState = SavedState.NotSaved) }
    }

}