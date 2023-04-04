package com.phoenix.newsapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.phoenix.newsapp.data.model.Article

@Database(entities = [Article::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract val articleBookmarkDao: ArticleBookmarkDao
}