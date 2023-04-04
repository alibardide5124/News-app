package com.phoenix.newsapp.ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.phoenix.newsapp.data.model.Article
import kotlinx.coroutines.flow.Flow

@Composable
fun FavoriteListComposable(
    items: Flow<PagingData<Article>>,
    listState: LazyListState,
    onItemClick: (Article) -> Unit
) {
    val lazyItems = items.collectAsLazyPagingItems()
//    Initial loading page
    AnimatedVisibility(
        visible = lazyItems.itemCount == 0 && lazyItems.loadState.refresh is LoadState.Loading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Loading saved news...",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
//    Error on initial loading
    AnimatedVisibility(
        visible = lazyItems.loadState.refresh is LoadState.Error,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Couldn't load saved articles :(",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { lazyItems.refresh() }) {
                Text("Retry")
            }
        }
    }
//    Empty list
    AnimatedVisibility(
        visible = lazyItems.itemCount == 0 && lazyItems.loadState.refresh is LoadState.NotLoading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No favorites :(",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "You might like to consider add some news next time",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(0.8f)
            )
        }
    }
//    News list
    AnimatedVisibility(visible = lazyItems.itemCount != 0, enter = fadeIn(), exit = fadeOut()) {
        LazyColumn(state = listState) {
            items(lazyItems) { article ->
                if (article!!.source.id != null)
                    HeadlineVerified(article) { onItemClick(article) }
                else
                    Headline(article) { onItemClick(article) }
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
        }
    }
}