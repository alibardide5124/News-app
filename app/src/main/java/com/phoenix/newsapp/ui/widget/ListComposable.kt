package com.phoenix.newsapp.ui.widget

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.phoenix.newsapp.data.model.Article
import kotlinx.coroutines.flow.Flow

@Composable
fun ListComposable(items: Flow<PagingData<Article>>) {
    val lazyItems: LazyPagingItems<Article> = items.collectAsLazyPagingItems()

    LazyColumn {
        items(lazyItems) { article ->
            if (article!!.source.id != null) HeadlineVerified(article)
            else Headline(article)
        }
    }
}