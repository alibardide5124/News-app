package com.phoenix.newsapp.screen.browser

import android.content.Context

sealed interface BrowserUiEvent {
    data class OnClickShareLink(val context: Context): BrowserUiEvent
    data object OnClickFavorite: BrowserUiEvent
    data class OnClickCopyLink(val context: Context): BrowserUiEvent
    data class OnClickOpenInBrowser(val context: Context): BrowserUiEvent
    data object PageLoadingStarted: BrowserUiEvent
    data object PageLoadingFinished: BrowserUiEvent
    data object PageLoadingError: BrowserUiEvent
}