package com.phoenix.newsapp.screen.browser

import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenix.newsapp.R
import com.phoenix.newsapp.components.ErrorView
import com.phoenix.newsapp.data.model.Article
import kotlinx.coroutines.launch

@Composable
fun BrowserScreen(
    browserViewModel: BrowserViewModel = hiltViewModel(),
    article: Article,
    onClickSave: (Boolean) -> Unit = {},
    dismissArticle: () -> Unit
) {
    val uiState by browserViewModel.uiState.collectAsState()
    val context = LocalContext.current

    browserViewModel.updateContent(article)
    browserViewModel.isArticleExistsInFavorite(article.url)

    BackHandler {
        dismissArticle()
    }

    BrowserNewsContent(
        context,
        isScreenExpanded = true,
        onNavigateBack = { dismissArticle() },
        article,
        isSaved = uiState.isSaved,
        isPageLoadingFinished = uiState.isLoadingFinished,
        isPageError = uiState.isLoadingError,
        onClickShare = { browserViewModel.onEvent(BrowserUiEvent.OnClickShareLink(context)) },
        onCopyLink = { browserViewModel.onEvent(BrowserUiEvent.OnClickCopyLink(context)) },
        onClickSave = {
            onClickSave(uiState.isSaved)
            browserViewModel.onEvent(BrowserUiEvent.OnClickFavorite)
        },
        onClickOpenInBrowser = {
            browserViewModel.onEvent(BrowserUiEvent.OnClickOpenInBrowser(context))
        },
        onPageLoadStart = { browserViewModel.onEvent(BrowserUiEvent.PageLoadingStarted) },
        onPageLoadFinished = { browserViewModel.onEvent(BrowserUiEvent.PageLoadingFinished) },
        onPageError = { browserViewModel.onEvent(BrowserUiEvent.PageLoadingError) }
    )
}

@Composable
fun BrowserExpandedScreen(
    browserViewModel: BrowserViewModel = hiltViewModel(),
    article: Article,
    onClickSave: (Boolean) -> Unit = {},
    dismissArticle: () -> Unit
) {
    val uiState by browserViewModel.uiState.collectAsState()
    val context = LocalContext.current

    browserViewModel.updateContent(article)
    browserViewModel.isArticleExistsInFavorite(article.url)

    BackHandler {
        dismissArticle()
    }

    BrowserNewsContent(
        context,
        isScreenExpanded = true,
        onNavigateBack = { dismissArticle() },
        article,
        isSaved = uiState.isSaved,
        isPageLoadingFinished = uiState.isLoadingFinished,
        isPageError = uiState.isLoadingError,
        onClickShare = { browserViewModel.onEvent(BrowserUiEvent.OnClickShareLink(context)) },
        onCopyLink = { browserViewModel.onEvent(BrowserUiEvent.OnClickCopyLink(context)) },
        onClickSave = {
            onClickSave(uiState.isSaved)
            browserViewModel.onEvent(BrowserUiEvent.OnClickFavorite)
        },
        onClickOpenInBrowser = {
            browserViewModel.onEvent(BrowserUiEvent.OnClickOpenInBrowser(context))
        },
        onPageLoadStart = { browserViewModel.onEvent(BrowserUiEvent.PageLoadingStarted) },
        onPageLoadFinished = { browserViewModel.onEvent(BrowserUiEvent.PageLoadingFinished) },
        onPageError = { browserViewModel.onEvent(BrowserUiEvent.PageLoadingError) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserNewsContent(
    context: Context,
    isScreenExpanded: Boolean,
    onNavigateBack: () -> Unit = {},
    article: Article,
    isSaved: Boolean,
    isPageLoadingFinished: Boolean,
    isPageError: Boolean,
    onClickShare: () -> Unit,
    onCopyLink: () -> Unit,
    onClickSave: () -> Unit,
    onClickOpenInBrowser: () -> Unit,
    onPageLoadStart: () -> Unit,
    onPageLoadFinished: () -> Unit,
    onPageError: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutine = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = article.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 15.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            imageVector =
                            if (isScreenExpanded) Icons.Outlined.KeyboardArrowLeft else Icons.Default.Close,
                            contentDescription = null,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { onClickShare() }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                    }
                    IconButton(
                        onClick = {
                            coroutine.launch {
                                snackBarHostState.showSnackbar(message = "Link copied")
                            }
                            onCopyLink()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_copy),
                            contentDescription = "Copy link",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    IconButton(onClick = { onClickSave() }) {
                        Icon(
                            painter = painterResource(
                                if (isSaved) R.drawable.ic_bookmark
                                else R.drawable.ic_bookmark_outline
                            ),
                            contentDescription = "Save to favorites"
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = { onClickOpenInBrowser() }) {
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
            val webView = remember { WebView(context) }
            AndroidView(
                factory = {
                    webView.apply {
                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(
                                view: WebView?,
                                url: String?,
                                favicon: Bitmap?
                            ) {
                                super.onPageStarted(view, url, favicon)
                                onPageLoadStart()
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                onPageLoadFinished()
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                onPageError()
                            }
                        }
                        loadUrl(article.url)
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                })
            if (!isPageLoadingFinished)
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            AnimatedVisibility(
                visible = isPageError,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    ErrorView(title = "There was an error loading page") {
                        webView.loadUrl(article.url)
                    }
                }
            }
        }
    }

}