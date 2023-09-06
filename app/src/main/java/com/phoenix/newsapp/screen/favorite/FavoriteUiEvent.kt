package com.phoenix.newsapp.screen.favorite

import com.phoenix.newsapp.data.model.Article

sealed interface FavoriteUiEvent {
    data class OnClickFavorite(val isSaved: Boolean): FavoriteUiEvent
    data class OnSelectArticle(val article: Article): FavoriteUiEvent
    data object OnCloseArticle: FavoriteUiEvent
    data object OnDataLoaded: FavoriteUiEvent
}