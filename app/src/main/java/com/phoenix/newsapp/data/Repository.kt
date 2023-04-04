package com.phoenix.newsapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.phoenix.newsapp.data.model.Article
import com.phoenix.newsapp.Constants
import com.phoenix.newsapp.data.database.ArticleBookmarkDao
import com.phoenix.newsapp.data.database.FavoritePagingSource
import com.phoenix.newsapp.data.network.EverythingPagingSource
import com.phoenix.newsapp.data.network.HeadlinesPagingSource
import com.phoenix.newsapp.data.network.NewsApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val newsApi: NewsApi,
    private val articleBookmarkDao: ArticleBookmarkDao
) {

    fun getTopHeadlines(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.DEFAULT_PAGE_SIZE,
            ),
            pagingSourceFactory = {
                HeadlinesPagingSource(newsApi)
            }
        ).flow
    }

    fun getEverything(query: String): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.DEFAULT_PAGE_SIZE,
            ),
            pagingSourceFactory = {
                EverythingPagingSource(newsApi, query)
            }
        ).flow
    }

    fun getSavedArticles(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.DEFAULT_PAGE_SIZE,
            ),
            pagingSourceFactory = {
                FavoritePagingSource(articleBookmarkDao)
            }
        ).flow
    }

}