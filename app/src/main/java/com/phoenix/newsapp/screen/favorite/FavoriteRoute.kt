package com.phoenix.newsapp.screen.favorite

import android.app.Activity
import androidx.activity.compose.BackHandler
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
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Destination(route = NavGraphDestination.SAVES_ROUTE)
@Composable
fun FavoriteRoute(
    navigator: DestinationsNavigator,
    favoriteViewModel: FavoriteViewModel = hiltViewModel()
) {
    val activity = LocalContext.current as Activity
    val windowWidthSizeClass = calculateWindowSizeClass(activity).widthSizeClass
    val uiState by favoriteViewModel.uiState.collectAsState()
    val listState = rememberLazyGridState(uiState.firstVisibleItem)

    LaunchedEffect(listState) {
        favoriteViewModel.updateListState(listState.firstVisibleItemIndex)
    }
    favoriteViewModel.updateNavigator(navigator)

    BackHandler(uiState.selectedArticle == null) {
        navigator.navigateUp()
    }

    if (windowWidthSizeClass == WindowWidthSizeClass.Expanded)
        FavoriteWithBrowser(
            onNavigateBack = { navigator.navigateUp() },
            lazyGridState = listState,
            listItems = favoriteViewModel.savedArticles,
            onClickArticle = { favoriteViewModel.onEvent(FavoriteUiEvent.OnSelectArticle(it) )},
            selectedArticle = uiState.selectedArticle,
            dismissArticle = { favoriteViewModel.onEvent(FavoriteUiEvent.OnCloseArticle) },
            onClickSave = { favoriteViewModel.onEvent(FavoriteUiEvent.OnClickFavorite(it))}
        )
    else
        FavoriteList(
            onNavigateBack = { navigator.navigateUp() },
            lazyGridState = listState,
            listItems = favoriteViewModel.savedArticles,
            onClickArticle = { favoriteViewModel.onEvent(FavoriteUiEvent.OnSelectArticle(it) )},
            selectedArticle = uiState.selectedArticle,
            dismissArticle = { favoriteViewModel.onEvent(FavoriteUiEvent.OnCloseArticle) },
            onClickSave = { favoriteViewModel.onEvent(FavoriteUiEvent.OnClickFavorite(it))}
        )
}