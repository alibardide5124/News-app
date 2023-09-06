package com.phoenix.newsapp.screen.home

import com.phoenix.newsapp.data.model.Article

data class HomeUiState(
    val firstVisibleItem: Int = 0,
    val selectedPost: Article? = null
)