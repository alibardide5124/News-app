package com.phoenix.newsapp.screen.home

import com.phoenix.newsapp.data.model.Article

sealed interface HomeUiEvent {
    data object GoToSearchScreen: HomeUiEvent
    data object GoToAboutScreen: HomeUiEvent
    data object GoToFavoritesScreen: HomeUiEvent
    data class OnSelectArticle(val article: Article): HomeUiEvent
    data object OnCloseArticle: HomeUiEvent
}