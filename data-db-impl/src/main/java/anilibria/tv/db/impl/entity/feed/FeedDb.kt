package anilibria.tv.db.impl.entity.feed

import androidx.room.*
import anilibria.tv.db.impl.entity.release.FlatReleaseDb
import anilibria.tv.db.impl.entity.youtube.YoutubeDb

@Entity(
    tableName = "feed",
    foreignKeys = [
        ForeignKey(
            entity = FlatReleaseDb::class,
            parentColumns = ["releaseId"],
            childColumns = ["releaseId"]
        ),
        ForeignKey(
            entity = YoutubeDb::class,
            parentColumns = ["youtubeId"],
            childColumns = ["youtubeId"]
        )
    ],
    indices = [Index("releaseId"), Index("youtubeId")]
)
data class FeedDb(
    @PrimaryKey @ColumnInfo(name = "key") val key: String,
    @ColumnInfo(name = "releaseId") val releaseId: Int?,
    @ColumnInfo(name = "youtubeId") val youtubeId: Int?
)