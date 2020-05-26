package ru.radiationx.data.adb.feed

import androidx.room.Embedded
import androidx.room.Relation
import ru.radiationx.data.adb.release.FlatReleaseDb
import ru.radiationx.data.adb.release.ReleaseDb
import ru.radiationx.data.adb.youtube.YouTubeDb

data class FeedDb(
    @Embedded val feed: FlatFeedDb,

    @Relation(
        entity = FlatReleaseDb::class,
        parentColumn = "releaseId",
        entityColumn = "releaseId"
    )
    val release: ReleaseDb?,

    @Relation(
        entity = YouTubeDb::class,
        parentColumn = "youtubeId",
        entityColumn = "youtubeId"
    )
    val youtube: YouTubeDb?
)