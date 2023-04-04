package com.phoenix.newsapp.ui.screen.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenix.newsapp.R
import com.phoenix.newsapp.data.model.Article
import com.phoenix.newsapp.ui.bottomsheet.news.BottomSheetContent
import com.phoenix.newsapp.ui.bottomsheet.news.SavedState
import com.phoenix.newsapp.ui.widget.ListComposable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SearchScreen(
    navigator: DestinationsNavigator,
    viewModel: SearchViewModel = hiltViewModel()
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

    Scaffold(
        topBar = {
            SearchTopAppBar(
                text = uiState.searchedQuery,
                onTextChange = {
                    viewModel.onEvent(SearchUiEvent.OnSearchQueryChange(it))
                }, onSearchClicked = {
                    viewModel.onEvent(SearchUiEvent.OnHitSearch)
                }, onCloseClicked = {
                    navigator.popBackStack()
                }
            )
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            Box(
                modifier = Modifier
                    .height(6.dp)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0x21212121), Color.Transparent
                            )
                        )
                    )
            )
            AnimatedVisibility(
                visible = uiState.isSearched.not(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                EmptySearchIllustration()
            }
            AnimatedVisibility(visible = uiState.isSearched, enter = fadeIn(), exit = fadeOut()) {
                ListComposable(items = viewModel.searchedArticles, listState) { article ->
                    currentItem = article
                    coroutineScope.launch {
                        sheetState.show()
                        viewModel.isArticleExistsInFavorite(article.url)
                    }
                }
            }
        }
    }
    if (sheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {},
            sheetState = sheetState
        ) {
            BottomSheetContent(currentItem!!, newsSheetUiState) {
                if (newsSheetUiState.savedState == SavedState.Saved)
                    viewModel.deleteArticle(currentItem!!)
                else if (newsSheetUiState.savedState == SavedState.NotSaved)
                    viewModel.insertArticle(currentItem!!)
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
                Modifier
                    .alpha(0.54f)
                    .clickable { onSearchClicked() }
            )
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