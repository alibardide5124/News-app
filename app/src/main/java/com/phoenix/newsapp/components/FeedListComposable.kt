package com.phoenix.newsapp.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.phoenix.newsapp.R
import com.phoenix.newsapp.data.model.Article
import kotlinx.coroutines.flow.Flow

@Composable
fun FeedListComposable(
    items: Flow<PagingData<Article>>,
    listState: LazyGridState,
    onClickArticle: (Article) -> Unit,
    modifier: Modifier = Modifier,
    extraCondition: Pair<Boolean, @Composable () -> Unit> = false to {},
    browserContent: @Composable () -> Unit = {},
    content: @Composable (@Composable () -> Unit) -> Unit
) {
    val lazyItems = items.collectAsLazyPagingItems()

    AnimatedContent(targetState = lazyItems, label = "") { targetState ->
        when {
            extraCondition.first ->
                extraCondition.second

            targetState.loadState.refresh is LoadState.Loading ->
                ListLoading(
                    title = stringResource(id = R.string.loading_title),
                    message = stringResource(id = R.string.loading_massage)
                )

            targetState.loadState.refresh is LoadState.Error ->
                ErrorView(
                    title = "Can\'t load news",
                    message = "Check if you\'re connected to network"
                ) {
                    lazyItems.refresh()
                }


            targetState.itemCount != 0 ->
                content {
                    Row {
                        ListContent(
                            listState = listState,
                            lazyItems = targetState,
                            onItemClick = onClickArticle,
                            listType = ListType.Feed,
                            modifier = modifier,
                            extraContent = {
//                              Error on loading next page
                                if (lazyItems.loadState.append is LoadState.Error && lazyItems.itemCount < 90)
                                    item {
                                        HeadlineError { lazyItems.retry() }
                                    }
//                              Message at the end of list (newsapi only support 100 items per request)
                                if (lazyItems.loadState.append is LoadState.Error && lazyItems.itemCount >= 90)
                                    item {
                                        HeadlineEnd()
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                            }
                        )
                        browserContent()
                    }
                }
        }

    }
}
