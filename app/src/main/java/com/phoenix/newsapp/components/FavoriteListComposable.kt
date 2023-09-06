package com.phoenix.newsapp.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.phoenix.newsapp.data.model.Article
import kotlinx.coroutines.flow.Flow

@Composable
fun FavoriteListComposable(
    items: Flow<PagingData<Article>>,
    listState: LazyGridState,
    onClickArticle: (Article) -> Unit,
    modifier: Modifier = Modifier,
    browserContent: @Composable () -> Unit = {},
    content: @Composable (@Composable () -> Unit) -> Unit
) {
    val lazyItems = items.collectAsLazyPagingItems()

    AnimatedContent(targetState = lazyItems, label = "") { targetState ->
        when (targetState.loadState.refresh) {
            is LoadState.Error ->
                ErrorView(title = "Couldn't load saved articles :(") {
                    lazyItems.refresh()
                }

            LoadState.Loading ->
                    ListLoading(title = "Loading saved articles...")

            is LoadState.NotLoading -> {
                content {
                    if (targetState.itemCount == 0)
                        EmptyList(title = "No favorites :(")
                    else
                        Row {
                            ListContent(
                                listState = listState,
                                lazyItems = targetState,
                                onItemClick = onClickArticle,
                                listType = ListType.Favorite,
                                modifier = modifier
                            )
                            browserContent()
                        }
                }
            }
        }
    }
}
