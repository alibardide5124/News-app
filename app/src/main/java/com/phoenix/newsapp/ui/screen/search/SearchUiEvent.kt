package com.phoenix.newsapp.ui.screen.search

sealed class SearchUiEvent {
    data class OnSearchQueryChange(val query: String): SearchUiEvent()
    object OnHitSearch: SearchUiEvent()
    object GoToHomeScreen: SearchUiEvent()
}