package com.phoenix.newsapp.screen.browser

import com.phoenix.newsapp.data.model.Article

data class BrowserUiState(
    val article: Article? = null,
    val isSaved: Boolean = false,
    val initialSaveState: Boolean = false,
    val isLoadingFinished: Boolean = false
)