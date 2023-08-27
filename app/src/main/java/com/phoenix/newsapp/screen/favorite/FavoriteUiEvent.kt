package com.phoenix.newsapp.screen.favorite

import com.phoenix.newsapp.data.model.Article

sealed interface FavoriteUiEvent {
    data class OnItemClick(val article: Article): FavoriteUiEvent
}