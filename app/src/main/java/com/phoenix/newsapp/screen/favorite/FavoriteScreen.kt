package com.phoenix.newsapp.screen.favorite

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import com.phoenix.newsapp.components.FavoriteListComposable
import com.phoenix.newsapp.data.model.Article
import com.phoenix.newsapp.screen.browser.BrowserExpandedScreen
import com.phoenix.newsapp.screen.browser.BrowserScreen
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun FavoriteList(
    onNavigateBack: () -> Unit,
    lazyGridState: LazyGridState,
    listItems: MutableStateFlow<PagingData<Article>>,
    onClickArticle: (Article) -> Unit,
    selectedArticle: Article?,
    dismissArticle: () -> Unit,
    onClickSave: (Boolean) -> Unit
) {
    Crossfade(targetState = selectedArticle, label = "") {
        if (it != null)
            BrowserScreen(
                article = it,
                onClickSave = { isSaved -> onClickSave(isSaved) },
                dismissArticle = { dismissArticle() }
            )
        else
            FavoriteScreenContent(
                isTopBar = true,
                onNavigateBack = { onNavigateBack() },
                lazyGridState = lazyGridState,
                listItems = listItems,
                onClickArticle = onClickArticle
            )
    }
}

@Composable
fun FavoriteWithBrowser(
    onNavigateBack: () -> Unit,
    lazyGridState: LazyGridState,
    listItems: MutableStateFlow<PagingData<Article>>,
    onClickArticle: (Article) -> Unit,
    selectedArticle: Article?,
    dismissArticle: () -> Unit,
    onClickSave: (Boolean) -> Unit
) {
    FavoriteScreenContent(
        isTopBar = false,
        onNavigateBack = { onNavigateBack() },
        lazyGridState = lazyGridState,
        listItems = listItems,
        onClickArticle = onClickArticle,
        modifier = Modifier.width(334.dp),
        browserContent = {
            Crossfade(targetState = selectedArticle, label = "") {
                if (it != null)
                    BrowserExpandedScreen(
                        article = it,
                        onClickSave = { isSaved -> onClickSave(isSaved) },
                        dismissArticle = { dismissArticle() }
                    )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreenContent(
    isTopBar: Boolean,
    onNavigateBack: () -> Unit,
    lazyGridState: LazyGridState,
    listItems: MutableStateFlow<PagingData<Article>>,
    onClickArticle: (Article) -> Unit,
    modifier: Modifier = Modifier,
    browserContent: @Composable () -> Unit = {}
) {
    Scaffold(
        topBar = {
            if (isTopBar)
                CenterAlignedTopAppBar(
                    title = {
                        Text("Favorites")
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { onNavigateBack() },
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.KeyboardArrowLeft,
                                contentDescription = null,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
        }
    ) { paddingValues ->
        FavoriteListComposable(
            items = listItems,
            listState = lazyGridState,
            onClickArticle = { article -> onClickArticle(article) },
            modifier = modifier,
            browserContent = { browserContent() }
        ) { listContent ->
            Box(
                modifier = Modifier.padding(paddingValues)
            ) {
                listContent()
            }
        }
    }
}
