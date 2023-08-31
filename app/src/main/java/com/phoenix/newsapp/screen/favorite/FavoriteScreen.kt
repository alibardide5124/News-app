package com.phoenix.newsapp.screen.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenix.newsapp.screen.destinations.BrowserScreenDestination
import com.phoenix.newsapp.widget.FavoriteListComposable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient

@Destination
@Composable
fun FavoriteScreen(
    navigator: DestinationsNavigator,
    resultRecipient: ResultRecipient<BrowserScreenDestination, Boolean>,
    favoriteViewModel: FavoriteViewModel = hiltViewModel()
) {
    val uiState by favoriteViewModel.uiState.collectAsState()
    val listState = rememberLazyListState(uiState.firstVisibleItem)

    resultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {}

            is NavResult.Value -> {
                if (!result.value)
                    favoriteViewModel.onEvent(FavoriteUiEvent.Refresh)
            }
        }
    }

    LaunchedEffect(listState) {
        favoriteViewModel.updateListState(listState.firstVisibleItemIndex)
    }
    favoriteViewModel.updateNavigator(navigator)

    Scaffold(
        topBar = { FavoriteTopBar(
            onBackClick = { navigator.navigateUp() }
        ) }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            FavoriteListComposable(
                items = favoriteViewModel.savedArticles,
                listState = listState,
                onItemClick = { article ->
                    favoriteViewModel.onEvent(FavoriteUiEvent.OnItemClick(article))
                }
            )
        }
    }
}

@Composable
private fun FavoriteTopBar(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        IconButton(
            onClick = { onBackClick() },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowLeft,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
        Text(
            text = "Favorites",
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
