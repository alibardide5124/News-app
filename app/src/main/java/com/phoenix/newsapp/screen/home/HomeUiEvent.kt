package com.phoenix.newsapp.screen.home

sealed class HomeUiEvent {
    data object GoToSearchScreen: HomeUiEvent()
    data object GoToAboutScreen: HomeUiEvent()
    data object GoToFavoritesScreen: HomeUiEvent()
}