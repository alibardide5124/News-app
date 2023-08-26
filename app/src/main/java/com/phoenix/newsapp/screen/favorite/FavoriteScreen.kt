package com.phoenix.newsapp.screen.favorite

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenix.newsapp.BottomSheets
import com.phoenix.newsapp.data.model.Article
import com.phoenix.newsapp.widget.FavoriteListComposable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun FavoriteScreen(
    navigator: DestinationsNavigator,
    viewModel: FavoriteViewModel = hiltViewModel()
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
        topBar = { FavoriteTopBar { navigator.popBackStack() } }
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
            FavoriteListComposable(items = viewModel.savedArticles, listState) { article ->
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
            BottomSheets.BottomSheetContent(currentItem!!, newsSheetUiState) {
                if (newsSheetUiState.savedState == BottomSheets.SavedState.Saved)
                    viewModel.deleteArticle(currentItem!!)
                else if (newsSheetUiState.savedState == BottomSheets.SavedState.NotSaved)
                    viewModel.insertArticle(currentItem!!)
            }
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
