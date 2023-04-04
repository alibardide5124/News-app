package com.phoenix.newsapp.ui.screen.search

data class SearchUiState(
    val isSearched: Boolean = false,
    val searchedQuery: String = "",
    val firstVisibleItem: Int = 0
)