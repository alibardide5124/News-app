package com.phoenix.newsapp.ui.bottomsheet.news

data class NewsSheetUiState(
    val savedState: SavedState = SavedState.Loading,
)

enum class SavedState {
    Loading, Saved, NotSaved
}