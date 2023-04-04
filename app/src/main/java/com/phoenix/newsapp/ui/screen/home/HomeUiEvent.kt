package com.phoenix.newsapp.ui.screen.home

sealed class HomeUiEvent {
    object GoToSearchScreen: HomeUiEvent()
    object GoToAboutScreen: HomeUiEvent()
    object GoToFavoritesScreen: HomeUiEvent()
}