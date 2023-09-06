package com.phoenix.newsapp.screen.search

import com.phoenix.newsapp.data.model.Article

sealed interface SearchUiEvent {
    data class OnSearchQueryChange(val query: String): SearchUiEvent
    data object OnHitSearch: SearchUiEvent
    data object GoToHomeScreen: SearchUiEvent
    data class OnSelectArticle(val article: Article): SearchUiEvent
    data object OnCloseArticle: SearchUiEvent
}