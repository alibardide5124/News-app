package com.phoenix.newsapp.ui.screen

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Search: Screen("search")
    object About: Screen("about")
}