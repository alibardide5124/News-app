package com.phoenix.newsapp.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "articles")
data class Article(
    val author: String?,
    val content: String,
    val description: String,
    val publishedAt: String,
    @Embedded
    val source: Source,
    val title: String,
    @PrimaryKey
    val url: String,
    val urlToImage: String
)