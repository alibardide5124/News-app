package com.phoenix.newsapp.screen.search

data class SearchUiState(
    val isSearched: Boolean = false,
    val searchedQuery: String = "",
    val firstVisibleItem: Int = 0
)