package com.phoenix.newsapp.data.network

import com.phoenix.newsapp.BuildConfig
import com.phoenix.newsapp.data.network.response.NewsResponse
import com.phoenix.newsapp.ui.screen.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsApi {

    @GET(Constants.GET_TOP_HEADLINES)
    @Headers("x-api-key: ${BuildConfig.API_KEY}")
    suspend fun getTopHeadlines(
        @Query(Constants.PARAM_LANGUAGE) language: String = "en",
        @Query(Constants.PARAM_PAGE_SIZE) pageSize: Int,
        @Query(Constants.PARAM_PAGE) page: Int
    ): NewsResponse

    @GET(Constants.GET_EVERYTHING)
    @Headers("x-api-key: ${BuildConfig.API_KEY}")
    suspend fun getEverything(
        @Query(Constants.PARAM_QUERY) q: String,
        @Query(Constants.PARAM_PAGE_SIZE) pageSize: Int,
        @Query(Constants.PARAM_PAGE) page: Int
    ): NewsResponse

}