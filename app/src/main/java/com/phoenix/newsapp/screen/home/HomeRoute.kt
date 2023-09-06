package com.phoenix.newsapp.screen.home

import android.app.Activity
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenix.newsapp.NavGraphDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@RootNavGraph(start = true)
@Destination(route = NavGraphDestination.HOME_ROUTE)
@Composable
fun HomeRoute(
    navigator: DestinationsNavigator,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val activity = LocalContext.current as Activity
    val windowWidthSizeClass = calculateWindowSizeClass(activity).widthSizeClass
    val homeUiState by homeViewModel.uiState.collectAsState()
    val listState = rememberLazyGridState(homeUiState.firstVisibleItem)

    LaunchedEffect(listState) {
        homeViewModel.updateListState(listState.firstVisibleItemIndex)
    }
    homeViewModel.updateNavigator(navigator)

    if (windowWidthSizeClass == WindowWidthSizeClass.Expanded)
        HomeFeedWithBrowser(
            lazyGridState = listState,
            listItems = homeViewModel.topHeadlines,
            onClickArticle = { homeViewModel.onEvent(HomeUiEvent.OnSelectArticle(it))},
            selectedArticle = homeUiState.selectedPost,
            dismissArticle = { homeViewModel.onEvent(HomeUiEvent.OnCloseArticle) }
        )
    else
        HomeFeedList(
            onClickAbout = { homeViewModel.onEvent(HomeUiEvent.GoToAboutScreen) },
            onClickFavorite = { homeViewModel.onEvent(HomeUiEvent.GoToFavoritesScreen) },
            onClickSearch = { homeViewModel.onEvent(HomeUiEvent.GoToSearchScreen) },
            lazyGridState = listState,
            listItems = homeViewModel.topHeadlines,
            onClickArticle = { homeViewModel.onEvent(HomeUiEvent.OnSelectArticle(it)) },
            selectedArticle = homeUiState.selectedPost,
            dismissArticle = { homeViewModel.onEvent(HomeUiEvent.OnCloseArticle) }
        )
}