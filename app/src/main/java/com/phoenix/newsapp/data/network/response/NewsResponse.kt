package com.phoenix.newsapp.data.network.response

import com.phoenix.newsapp.data.model.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)