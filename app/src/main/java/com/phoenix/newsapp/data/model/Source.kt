package com.phoenix.newsapp.data.model

import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

@Serializable
data class Source(
    @ColumnInfo("sourceId") val id: String?,
    val name: String?
)