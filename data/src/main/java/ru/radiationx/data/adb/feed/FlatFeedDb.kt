package ru.radiationx.data.adb.feed

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ru.radiationx.data.adb.release.FlatReleaseDb
import ru.radiationx.data.adb.youtube.YouTubeDb

@Entity(
    tableName = "feed",
    foreignKeys = [
        ForeignKey(entity = FlatReleaseDb::class, parentColumns = ["releaseId"], childColumns = ["releaseId"]),
        ForeignKey(entity = YouTubeDb::class, parentColumns = ["youtubeId"], childColumns = ["youtubeId"])
    ]
)
data class FlatFeedDb(
    @ColumnInfo(name = "releaseId") val releaseId: Int,
    @ColumnInfo(name = "youtubeId") val youtubeId: Int
)