package ru.radiationx.data.adb.youtube

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "youtube")
data class YouTubeDb(
    @PrimaryKey @ColumnInfo(name = "youtubeId") val id: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "vid") val vid: String?,
    @ColumnInfo(name = "views") val views: Int,
    @ColumnInfo(name = "comments") val comments: Int,
    @ColumnInfo(name = "timestamp") val timestamp: Date
)