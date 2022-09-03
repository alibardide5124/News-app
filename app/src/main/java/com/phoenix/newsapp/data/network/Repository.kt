package com.phoenix.newsapp.data.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.phoenix.newsapp.data.model.Article
import com.phoenix.newsapp.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val newsApi: NewsApi
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

}