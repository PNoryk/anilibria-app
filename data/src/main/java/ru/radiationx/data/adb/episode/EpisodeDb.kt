package ru.radiationx.data.adb.episode

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "release_episode")
data class EpisodeDb(
    @PrimaryKey @ColumnInfo(name = "releaseId") val releaseId: Int,
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "sd") val sd: String?,
    @ColumnInfo(name = "hd") val hd: String?,
    @ColumnInfo(name = "fullhd") val fullhd: String?,
    @ColumnInfo(name = "srcSd") val srcSd: String?,
    @ColumnInfo(name = "srcHd") val srcHd: String?
)