package com.phoenix.newsapp.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenix.newsapp.R
import com.phoenix.newsapp.widget.ListComposable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()
    val listState = rememberLazyListState(uiState.firstVisibleItem)

    LaunchedEffect(listState) {
        homeViewModel.updateListState(listState.firstVisibleItemIndex)
    }
    homeViewModel.updateNavigator(navigator)

    Scaffold(
        topBar = { HomeTopBar(
            onClickAbout = { homeViewModel.onEvent(HomeUiEvent.GoToAboutScreen) },
            onClickFavorite = { homeViewModel.onEvent(HomeUiEvent.GoToFavoritesScreen) },
            onClickSearch = { homeViewModel.onEvent(HomeUiEvent.GoToSearchScreen) }
        ) }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            ListComposable(
                items = homeViewModel.topHeadlines,
                listState = listState,
                onItemClick = { homeViewModel.onEvent(HomeUiEvent.OnClickItem(it)) }
            )
        }
    }
}

@Composable
private fun HomeTopBar(
    onClickAbout: () -> Unit,
    onClickFavorite: () -> Unit,
    onClickSearch: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(modifier = Modifier.align(Alignment.CenterStart)) {
            IconButton(onClick = { onClickAbout() }) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
            IconButton(onClick = { onClickFavorite() }) {
                Icon(
                    painterResource(id = R.drawable.ic_bookmark),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
        Text(
            text = stringResource(R.string.app_name),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
        IconButton(
            onClick = { onClickSearch() },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
