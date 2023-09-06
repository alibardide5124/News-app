package com.phoenix.newsapp.screen.search

import android.app.Activity
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenix.newsapp.NavGraphDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Destination(route = NavGraphDestination.SEARCH_ROUTE)
@Composable
fun SearchRoute(
    navigator: DestinationsNavigator,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val activity = LocalContext.current as Activity
    val windowWidthSizeClass = calculateWindowSizeClass(activity).widthSizeClass
    val searchUiState by searchViewModel.uiState.collectAsState()
    val listState = rememberLazyGridState(searchUiState.firstVisibleItem)

    LaunchedEffect(listState) {
        searchViewModel.updateListState(listState.firstVisibleItemIndex)
    }
    searchViewModel.updateNavigator(navigator)

    if (windowWidthSizeClass == WindowWidthSizeClass.Expanded)
        SearchFeedWithBrowser(
            searchedQuery = searchUiState.searchedQuery,
            onSearchQueryChange = { searchViewModel.onEvent(SearchUiEvent.OnSearchQueryChange(it))},
            onSearch = { searchViewModel.onEvent(SearchUiEvent.OnHitSearch)},
            onNavigateUp = { navigator.navigateUp() },
            isSearched = searchUiState.isSearched,
            searchResults = searchViewModel.searchedArticles,
            lazyGridState = listState,
            onClickArticle = { searchViewModel.onEvent(SearchUiEvent.OnSelectArticle(it)) },
            selectedArticle = searchUiState.selectedArticle,
            dismissArticle = { searchViewModel.onEvent(SearchUiEvent.OnCloseArticle)}
        )
    else
        SearchFeedList(
            searchedQuery = searchUiState.searchedQuery,
            onSearchQueryChange = { searchViewModel.onEvent(SearchUiEvent.OnSearchQueryChange(it))},
            onSearch = { searchViewModel.onEvent(SearchUiEvent.OnHitSearch)},
            onNavigateUp = { navigator.navigateUp() },
            isSearched = searchUiState.isSearched,
            searchResults = searchViewModel.searchedArticles,
            lazyGridState = listState,
            onClickArticle = { searchViewModel.onEvent(SearchUiEvent.OnSelectArticle(it)) },
            selectedArticle = searchUiState.selectedArticle,
            dismissArticle = { searchViewModel.onEvent(SearchUiEvent.OnCloseArticle)}
        )
}