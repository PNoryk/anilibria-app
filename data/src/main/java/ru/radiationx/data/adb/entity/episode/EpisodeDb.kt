package ru.radiationx.data.adb.entity.episode

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "release_episode",
    primaryKeys = ["releaseId", "id"]
)
data class EpisodeDb(
    @ColumnInfo(name = "releaseId") val releaseId: Int,
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "sd") val sd: String?,
    @ColumnInfo(name = "hd") val hd: String?,
    @ColumnInfo(name = "fullhd") val fullhd: String?,
    @ColumnInfo(name = "srcSd") val srcSd: String?,
    @ColumnInfo(name = "srcHd") val srcHd: String?
)