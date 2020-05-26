package ru.radiationx.data.adb.torrent

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.radiationx.data.adb.release.FlatReleaseDb
import java.util.*

@Entity(
    tableName = "release_torrent",
    primaryKeys = ["releaseId", "id"],
    foreignKeys = [
        ForeignKey(entity = FlatReleaseDb::class, parentColumns = ["releaseId"], childColumns = ["releaseId"])
    ]
)
data class TorrentDb(
    @ColumnInfo(name = "releaseId") val releaseId: Int,
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "hash") val hash: String?,
    @ColumnInfo(name = "leechers") val leechers: Int,
    @ColumnInfo(name = "seeders") val seeders: Int,
    @ColumnInfo(name = "completed") val completed: Int,
    @ColumnInfo(name = "quality") val quality: String?,
    @ColumnInfo(name = "series") val series: String?,
    @ColumnInfo(name = "size") val size: Long,
    @ColumnInfo(name = "time") val time: Date,
    @ColumnInfo(name = "url") val url: String?
)