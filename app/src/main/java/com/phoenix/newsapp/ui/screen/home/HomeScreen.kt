package com.phoenix.newsapp.ui.screen.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenix.newsapp.R
import com.phoenix.newsapp.data.model.Article
import com.phoenix.newsapp.ui.bottomsheet.news.BottomSheetContent
import com.phoenix.newsapp.ui.bottomsheet.news.SavedState
import com.phoenix.newsapp.ui.widget.ListComposable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val newsSheetUiState by viewModel.newsSheetUiState.collectAsState()
    val listState = rememberLazyListState(uiState.firstVisibleItem)

    LaunchedEffect(listState) {
        viewModel.updateListState(listState.firstVisibleItemIndex)
    }
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var currentItem: Article? by remember { mutableStateOf(null) }
    viewModel.updateNavigator(navigator)

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch {
            sheetState.hide()
        }
    }

    Scaffold(
        topBar = { HomeTopBar { viewModel.onEvent(it) } }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .height(6.dp)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0x21212121),
                                Color.Transparent
                            )
                        )
                    )
            )
            ListComposable(items = viewModel.topHeadlines, listState) { article ->
                currentItem = article
                coroutineScope.launch {
                    sheetState.show()
                    viewModel.isArticleExistsInFavorite(article.url)
                }
            }
        }
    }
    if (sheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {},
            sheetState = sheetState
        ) {
            BottomSheetContent(currentItem!!, newsSheetUiState) {
                if (newsSheetUiState.savedState == SavedState.Saved)
                    viewModel.deleteArticle(currentItem!!)
                else if (newsSheetUiState.savedState == SavedState.NotSaved)
                    viewModel.insertArticle(currentItem!!)
            }
        }
    }
}

@Composable
private fun HomeTopBar(onEvent: (HomeUiEvent) -> Unit) {
    Box(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(modifier = Modifier.align(Alignment.CenterStart)) {
            IconButton(onClick = { onEvent(HomeUiEvent.GoToAboutScreen) }) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
            IconButton(onClick = { onEvent(HomeUiEvent.GoToFavoritesScreen) }) {
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
            onClick = { onEvent(HomeUiEvent.GoToSearchScreen) },
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
