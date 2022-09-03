package com.phoenix.newsapp.ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.phoenix.newsapp.data.model.Article
import kotlinx.coroutines.flow.Flow

@Composable
fun ListComposable(items: Flow<PagingData<Article>>) {
    val lazyItems: LazyPagingItems<Article> = items.collectAsLazyPagingItems()
    AnimatedVisibility(visible = lazyItems.itemCount == 0, enter = fadeIn(), exit = fadeOut()) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Hang in there!",
                color = Color(0xDE000000),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Im getting some fresh news for you...",
                color = Color(0x8A000000),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
    AnimatedVisibility(visible = lazyItems.itemCount != 0, enter = fadeIn(), exit = fadeOut()) {
        LazyColumn {
            items(lazyItems) { article ->
                if (article!!.source.id != null) HeadlineVerified(article)
                else Headline(article)
            }
        }
    }
}