package com.phoenix.newsapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.phoenix.newsapp.data.model.Article

@Dao
interface ArticleBookmarkDao {

    @Insert
    suspend fun insertArticle(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("SELECT EXISTS (SELECT * FROM articles WHERE url=:url)")
    suspend fun isRowExist(url: String): Boolean

    @Query("SELECT * FROM articles LIMIT :limit OFFSET :offset")
    suspend fun getBookmarkedArticles(limit: Int = 10, offset: Int): List<Article>

}