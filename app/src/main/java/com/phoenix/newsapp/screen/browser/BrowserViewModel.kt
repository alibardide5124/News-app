package com.phoenix.newsapp.screen.browser

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.newsapp.data.database.ArticleBookmarkDao
import com.phoenix.newsapp.data.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowserViewModel @Inject constructor(
    private val articleBookmarkDao: ArticleBookmarkDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(BrowserUiState())
    val uiState = _uiState.asStateFlow()

    fun updateContent(article: Article) {
        _uiState.update { it.copy(article = article) }
    }

    fun onEvent(event: BrowserUiEvent) {
        when (event) {
            is BrowserUiEvent.OnClickCopyLink -> {
                val clipboardManager =
                    event.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("url", uiState.value.article!!.url)
                clipboardManager.setPrimaryClip(clip)
            }

            BrowserUiEvent.OnClickFavorite -> {
                viewModelScope.launch {
                    if (uiState.value.isSaved)
                        articleBookmarkDao.deleteArticle(uiState.value.article!!)
                    else
                        articleBookmarkDao.insertArticle(uiState.value.article!!)

                    _uiState.update { it.copy(isSaved = !it.isSaved) }
                }
            }

            is BrowserUiEvent.OnClickShareLink -> {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, uiState.value.article!!.url)
                }
                event.context.startActivity(Intent.createChooser(intent, "Share with:"))
            }

            is BrowserUiEvent.OnClickOpenInBrowser -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uiState.value.article!!.url))
                event.context.startActivity(browserIntent)
            }

            BrowserUiEvent.PageLoadingFinished ->
                _uiState.update { it.copy(isLoadingFinished = true) }

            BrowserUiEvent.PageLoadingError ->
                _uiState.update { it.copy(isLoadingError = true) }

            BrowserUiEvent.PageLoadingStarted ->
                _uiState.update { it.copy(isLoadingError = false) }

        }
    }

    fun isArticleExistsInFavorite(url: String) {
        viewModelScope.launch {
            val isRowExist = articleBookmarkDao.isRowExist(url)
            _uiState.update { it.copy(
                isSaved = isRowExist,
                initialSaveState = isRowExist
            ) }
        }
    }

}
