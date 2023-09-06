package com.phoenix.newsapp.screen.search

import com.phoenix.newsapp.data.model.Article

data class SearchUiState(
    val isSearched: Boolean = false,
    val searchedQuery: String = "",
    val firstVisibleItem: Int = 0,
    val selectedArticle: Article? = null
)