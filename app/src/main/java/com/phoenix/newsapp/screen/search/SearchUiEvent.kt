package com.phoenix.newsapp.screen.search

sealed class SearchUiEvent {
    data class OnSearchQueryChange(val query: String): SearchUiEvent()
    data object OnHitSearch: SearchUiEvent()
    data object GoToHomeScreen: SearchUiEvent()
}