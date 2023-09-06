package com.phoenix.newsapp.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.phoenix.newsapp.data.Repository
import com.phoenix.newsapp.data.model.Article
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteUiState())
    val uiState = _uiState.asStateFlow()
    private lateinit var navigator: DestinationsNavigator
    val savedArticles = MutableStateFlow<PagingData<Article>>(PagingData.empty())

    init {
        getSavedArticles()
    }

    fun onEvent(event: FavoriteUiEvent) {
        when(event) {
            is FavoriteUiEvent.OnSelectArticle ->
                _uiState.update { it.copy(selectedArticle = event.article) }

            FavoriteUiEvent.OnCloseArticle ->
                _uiState.update { it.copy(selectedArticle = null) }

            is FavoriteUiEvent.OnClickFavorite ->
                getSavedArticles()

            FavoriteUiEvent.OnDataLoaded ->
                _uiState.update { it.copy(dataLoaded = true) }
        }
    }

    private fun getSavedArticles() {
        viewModelScope.launch {
            repository.getSavedArticles()
                .cachedIn(viewModelScope).collect {
                    savedArticles.value = it
                }
        }
    }

    fun updateNavigator(navigator: DestinationsNavigator) {
        this.navigator = navigator
    }

    fun updateListState(index: Int) {
        _uiState.update { it.copy(firstVisibleItem = index) }
    }

}