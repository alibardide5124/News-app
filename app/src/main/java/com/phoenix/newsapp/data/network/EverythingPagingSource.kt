package com.phoenix.newsapp.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.phoenix.newsapp.data.model.Article
import com.phoenix.newsapp.Constants
import retrofit2.HttpException
import java.io.IOException

class EverythingPagingSource(
    private val newsApi: NewsApi,
    private val query: String
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val pageIndex = params.key ?: 1
        return try {
            val response =
                newsApi.getEverything(q = query,pageSize = Constants.DEFAULT_PAGE_SIZE, page = pageIndex)
            val articles = response.articles
            val prevKey =
                if (pageIndex == 1) null
                else pageIndex
            val nextKey =
                if (articles.isEmpty()) null
                else {
//                    pageIndex + (params.loadSize / Constants.DEFAULT_PAGE_SIZE)
                    pageIndex + 1
                }
            LoadResult.Page(data = articles, prevKey, nextKey)
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
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
