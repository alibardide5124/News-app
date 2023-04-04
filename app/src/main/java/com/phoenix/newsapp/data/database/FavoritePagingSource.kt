package com.phoenix.newsapp.data.database

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.phoenix.newsapp.data.model.Article
import java.io.IOException

class FavoritePagingSource(
    private val articleBookmarkDao: ArticleBookmarkDao
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val pageIndex = params.key ?: 1
        return try {
            val response = articleBookmarkDao.getBookmarkedArticles(offset = pageIndex)
            val prevKey =
                if (pageIndex == 1) null
                else pageIndex
            val nextKey =
                if (response.isEmpty()) null
                else pageIndex + 1
            LoadResult.Page(data = response, prevKey, nextKey)
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}
