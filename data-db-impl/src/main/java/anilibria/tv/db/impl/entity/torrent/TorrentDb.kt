package anilibria.tv.db.impl.entity.torrent

import androidx.room.*
import anilibria.tv.db.impl.entity.release.FlatReleaseDb
import java.util.*

@Entity(
    tableName = "release_torrent",
    foreignKeys = [
        ForeignKey(entity = FlatReleaseDb::class, parentColumns = ["releaseId"], childColumns = ["releaseId"])
    ],
    indices = [Index("releaseId"), Index("id")]
)
data class TorrentDb(
    @PrimaryKey @ColumnInfo(name = "key") val key: String,
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