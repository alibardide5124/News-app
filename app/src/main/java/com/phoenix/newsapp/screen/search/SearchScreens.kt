package com.phoenix.newsapp.screen.search

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import com.phoenix.newsapp.R
import com.phoenix.newsapp.components.FeedListComposable
import com.phoenix.newsapp.data.model.Article
import com.phoenix.newsapp.screen.browser.BrowserExpandedScreen
import com.phoenix.newsapp.screen.browser.BrowserScreen
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SearchFeedList(
    searchedQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onNavigateUp: () -> Unit,
    isSearched: Boolean,
    searchResults: MutableStateFlow<PagingData<Article>>,
    lazyGridState: LazyGridState,
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
            SearchScreenWithList(
                searchedQuery,
                onSearchQueryChange = { onSearchQueryChange(it) },
                onSearch = { onSearch() },
                onNavigateUp = { onNavigateUp() },
                isSearched,
                searchResults,
                lazyGridState,
                onClickArticle = { article -> onClickArticle(article) },
            )
    }
}

@Composable
fun SearchFeedWithBrowser(
    searchedQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onNavigateUp: () -> Unit,
    isSearched: Boolean,
    searchResults: MutableStateFlow<PagingData<Article>>,
    lazyGridState: LazyGridState,
    onClickArticle: (Article) -> Unit,
    selectedArticle: Article?,
    dismissArticle: () -> Unit
) {
    SearchScreenWithList(
        searchedQuery,
        onSearchQueryChange = { onSearchQueryChange(it) },
        onSearch = { onSearch() },
        onNavigateUp = { onNavigateUp() },
        isSearched,
        searchResults,
        lazyGridState,
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreenWithList(
    searchedQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onNavigateUp: () -> Unit,
    isSearched: Boolean,
    searchResults: MutableStateFlow<PagingData<Article>>,
    lazyGridState: LazyGridState,
    onClickArticle: (Article) -> Unit,
    modifier: Modifier = Modifier,
    browserContent: @Composable () -> Unit = {}
) {
        val keyboard = LocalSoftwareKeyboardController.current

        Scaffold(
            topBar = {
                SearchTopAppBar(
                    text = searchedQuery,
                    onTextChange = { onSearchQueryChange(it) },
                    onSearchClicked = {
                        onSearch()
                        keyboard?.hide()
                    },
                    onCloseClicked = { onNavigateUp() }
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                FeedListComposable(
                    items = searchResults,
                    listState = lazyGridState,
                    onClickArticle = { onClickArticle(it) },
                    modifier = modifier,
                    browserContent = browserContent,
                    extraCondition = !isSearched to { EmptySearchIllustration() }
                ) { listContent ->
                    listContent()
                }
            }
        }
}

@Composable
private fun SearchTopAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSearchClicked: () -> Unit,
    onCloseClicked: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 8.dp),
        value = text,
        onValueChange = { string: String ->
            onTextChange(string)
        },
        placeholder = {
            Text(
                text = "Search in the news...",
                modifier = Modifier
                    .alpha(0.54f)
                    .padding(0.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                Modifier.alpha(0.54f))
        },
        trailingIcon = {
            IconButton(onClick = { onCloseClicked() }) {
                Icon(
                    imageVector = Icons.Default.Close, contentDescription = null
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearchClicked() }),
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun EmptySearchIllustration() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.search),
            contentDescription = null,
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Search in the news",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Search among the thousands of news!\nfrom different & reliable sources",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}