package com.phoenix.newsapp.data.network

import com.phoenix.newsapp.BuildConfig
import com.phoenix.newsapp.data.network.response.NewsResponse
import com.phoenix.newsapp.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsApiService {

    @Headers("x-api-key: ${BuildConfig.API_KEY}")
    @GET(Constants.GET_TOP_HEADLINES)
    suspend fun getTopHeadlines(
        @Query(Constants.PARAM_PAGE_SIZE) pageSize: Int,
        @Query(Constants.PARAM_PAGE) page: Int
    ): NewsResponse

}