package com.phoenix.newsapp.screen.browser

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenix.newsapp.R
import com.phoenix.newsapp.data.model.Article
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun BrowserScreen(
    navigator: DestinationsNavigator,
    browserViewModel: BrowserViewModel = hiltViewModel(),
    article: Article
) {
    val uiState by browserViewModel.uiState.collectAsState()
    val context = LocalContext.current

    browserViewModel.updateContent(article)
    browserViewModel.isArticleExistsInFavorite(article.url)

    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(
                        onClick = {
                        browserViewModel.onEvent(BrowserUiEvent.OnClickShareLink(context))
                    }
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                    }
                    IconButton(
                        onClick = {
                            browserViewModel.onEvent(BrowserUiEvent.OnClickCopyLink(context))
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_copy),
                            contentDescription = "Copy link",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    IconButton(onClick = { browserViewModel.onEvent(BrowserUiEvent.OnClickFavorite) }) {
                        Icon(
                            painter = painterResource(
                                when (uiState.isSaved) {
                                    true -> R.drawable.ic_bookmark
                                    false -> R.drawable.ic_bookmark_outline
                                }
                            ),
                            contentDescription = "Save to favorites"
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { browserViewModel.onEvent(BrowserUiEvent.OnClickOpenInBrowser(context)) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_browser),
                                contentDescription = null
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(text = "Open in browser")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AndroidView(factory = {
                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            browserViewModel.onEvent(BrowserUiEvent.PageLoadingFinished)
                        }
                    }
                    loadUrl(article.url)
                }
            })
            if (!uiState.isLoadingFinished)
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            IconButton(
                onClick = { navigator.navigateUp() },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.padding(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = null
                )
            }
        }
    }

}