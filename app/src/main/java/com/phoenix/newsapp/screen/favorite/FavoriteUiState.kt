package com.phoenix.newsapp.screen.favorite

import com.phoenix.newsapp.data.model.Article

data class FavoriteUiState(
    val firstVisibleItem: Int = 0,
    val selectedArticle: Article? = null,
    val dataLoaded: Boolean = false
)