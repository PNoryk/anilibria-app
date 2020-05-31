package anilibria.tv.db.impl.entity.episode

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "release_episode",
    indices = [Index("releaseId"), Index("id")]
)
data class EpisodeDb(
    @PrimaryKey @ColumnInfo(name = "key") val key: String,
    @ColumnInfo(name = "releaseId") val releaseId: Int,
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "sd") val sd: String?,
    @ColumnInfo(name = "hd") val hd: String?,
    @ColumnInfo(name = "fullhd") val fullhd: String?,
    @ColumnInfo(name = "srcSd") val srcSd: String?,
    @ColumnInfo(name = "srcHd") val srcHd: String?
)