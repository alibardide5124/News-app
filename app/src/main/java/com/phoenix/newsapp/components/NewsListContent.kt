package com.phoenix.newsapp.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.phoenix.newsapp.data.model.Article

@Composable
fun ListContent(
    listState: LazyGridState,
    lazyItems: LazyPagingItems<Article>,
    onItemClick: (Article) -> Unit,
    listType: ListType,
    modifier: Modifier = Modifier,
    extraContent: (LazyGridScope.() -> Unit)? = null
) {
    LazyVerticalGrid(
        state = listState,
        columns = GridCells.Adaptive(334.dp),
        modifier = modifier
    ) {
        items(lazyItems.itemCount) { index ->
            val article = lazyItems[index]!!
            when(listType) {
                ListType.Feed -> {
                    if (article.source.id != null)
                        HeadlineVerified(article) { onItemClick(article) }
                    else
                        Headline(article) { onItemClick(article) }
                }
                ListType.Favorite ->
                    Headline(article) { onItemClick(article) }
            }
        }
//            Circle loading at the end of list
        if (lazyItems.loadState.append is LoadState.Loading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        if (extraContent != null)
            extraContent()
    }
}

enum class ListType {
    Feed, Favorite
}