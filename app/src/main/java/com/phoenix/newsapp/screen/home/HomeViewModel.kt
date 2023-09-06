package com.phoenix.newsapp.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.phoenix.newsapp.data.Repository
import com.phoenix.newsapp.data.model.Article
import com.phoenix.newsapp.screen.destinations.AboutScreenDestination
import com.phoenix.newsapp.screen.destinations.FavoriteRouteDestination
import com.phoenix.newsapp.screen.destinations.SearchRouteDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()
    private lateinit var navigator: DestinationsNavigator
    val topHeadlines = MutableStateFlow<PagingData<Article>>(PagingData.empty())

    init {
        getTopHeadlines()
    }

    fun updateNavigator(navigator: DestinationsNavigator) {
        this.navigator = navigator
    }

    fun updateListState(index: Int) {
        _uiState.update { it.copy(firstVisibleItem = index) }
    }

    private fun getTopHeadlines() {
        viewModelScope.launch {
            repository.getTopHeadlines()
                .cachedIn(viewModelScope).collect {
                    topHeadlines.value = it
                }
        }
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.GoToAboutScreen ->
                navigator.navigate(AboutScreenDestination) {
                    launchSingleTop = true
                }

            HomeUiEvent.GoToSearchScreen ->
                navigator.navigate(SearchRouteDestination) {
                    launchSingleTop = true
                }

            HomeUiEvent.GoToFavoritesScreen ->
                navigator.navigate(FavoriteRouteDestination) {
                    launchSingleTop = true
                }

            is HomeUiEvent.OnSelectArticle ->
                _uiState.update { it.copy(selectedPost = event.article) }

            HomeUiEvent.OnCloseArticle ->
                _uiState.update { it.copy(selectedPost = null) }
        }
    }

}