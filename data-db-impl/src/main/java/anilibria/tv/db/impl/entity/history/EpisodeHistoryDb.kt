package anilibria.tv.db.impl.entity.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import anilibria.tv.db.impl.entity.episode.EpisodeDb
import anilibria.tv.db.impl.entity.release.FlatReleaseDb
import java.util.*

@Entity(
    tableName = "episode_history",
    foreignKeys = [
        ForeignKey(entity = EpisodeDb::class, parentColumns = ["key"], childColumns = ["key"])
    ]
)
data class EpisodeHistoryDb(
    @PrimaryKey @ColumnInfo(name = "key") val key: String,
    @ColumnInfo(name = "releaseId") val releaseId: Int,
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "seek") val seek: Long,
    @ColumnInfo(name = "lastAccess") val lastAccess: Date,
    @ColumnInfo(name = "isViewed") val isViewed: Boolean
)