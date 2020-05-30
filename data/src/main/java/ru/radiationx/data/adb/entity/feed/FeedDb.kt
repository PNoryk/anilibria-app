package ru.radiationx.data.adb.entity.feed

import androidx.room.*
import ru.radiationx.data.adb.entity.release.FlatReleaseDb
import ru.radiationx.data.adb.entity.youtube.YoutubeDb

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