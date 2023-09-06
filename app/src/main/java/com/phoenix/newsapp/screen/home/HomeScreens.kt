package com.phoenix.newsapp.screen.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import com.phoenix.newsapp.R
import com.phoenix.newsapp.components.FeedListComposable
import com.phoenix.newsapp.data.model.Article
import com.phoenix.newsapp.screen.browser.BrowserExpandedScreen
import com.phoenix.newsapp.screen.browser.BrowserScreen
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun HomeFeedList(
    onClickAbout: () -> Unit,
    onClickFavorite: () -> Unit,
    onClickSearch: () -> Unit,
    lazyGridState: LazyGridState,
    listItems: MutableStateFlow<PagingData<Article>>,
    onClickArticle: (Article) -> Unit,
    selectedArticle: Article?,
    dismissArticle: () -> Unit
) {
    Crossfade(targetState = selectedArticle, label = "") {
        if (it != null)
            BrowserScreen(
                article = it,
                dismissArticle = { dismissArticle() }
            )
        else
            HomeScreenWithList(
                isTopAppBar = true,
                onClickAbout = { onClickAbout() },
                onClickFavorite = { onClickFavorite() },
                onClickSearch = { onClickSearch() },
                lazyGridState,
                listItems,
                onClickArticle = { article -> onClickArticle(article) }
            )
    }

}

@Composable
fun HomeFeedWithBrowser(
    lazyGridState: LazyGridState,
    listItems: MutableStateFlow<PagingData<Article>>,
    onClickArticle: (Article) -> Unit,
    selectedArticle: Article?,
    dismissArticle: () -> Unit
) {
    HomeScreenWithList(
        isTopAppBar = false,
        onClickAbout = null,
        onClickFavorite = null,
        onClickSearch = null,
        lazyGridState,
        listItems,
        onClickArticle = { onClickArticle(it) },
        modifier = Modifier.width(334.dp),
        browserContent = {
            Crossfade(targetState = selectedArticle, label = "") {
                if (it != null)
                    BrowserExpandedScreen(
                        article = it,
                        dismissArticle = { dismissArticle() }
                    )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenWithList(
    isTopAppBar: Boolean,
    onClickAbout: (() -> Unit)?,
    onClickFavorite: (() -> Unit)?,
    onClickSearch: (() -> Unit)?,
    lazyGridState: LazyGridState,
    listItems: MutableStateFlow<PagingData<Article>>,
    onClickArticle: (Article) -> Unit,
    modifier: Modifier = Modifier,
    browserContent: @Composable () -> Unit = {}
) {
    Scaffold(
        topBar = {
            if (isTopAppBar)
                CenterAlignedTopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    navigationIcon = {
                        Row {
                            IconButton(onClick = { onClickAbout?.invoke() }) {
                                Icon(
                                    imageVector = Icons.Outlined.Info,
                                    contentDescription = null,
                                )
                            }
                            IconButton(onClick = { onClickFavorite?.invoke() }) {
                                Icon(
                                    painterResource(id = R.drawable.ic_bookmark),
                                    contentDescription = null,
                                )
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = { onClickSearch?.invoke() }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
        },
    ) { innerPadding ->
        FeedListComposable(
            items = listItems,
            listState = lazyGridState,
            onClickArticle = { onClickArticle(it) },
            browserContent = { browserContent() },
            modifier = modifier
        ) { listContent ->
            Box(
                modifier = Modifier.padding(innerPadding)
            ) {
                listContent()
            }
        }
    }
}
