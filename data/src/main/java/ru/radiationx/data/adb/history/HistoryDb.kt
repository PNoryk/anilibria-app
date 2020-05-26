package ru.radiationx.data.adb.history

import androidx.room.Embedded
import androidx.room.Relation
import ru.radiationx.data.adb.feed.FlatFeedDb
import ru.radiationx.data.adb.release.FlatReleaseDb
import ru.radiationx.data.adb.release.ReleaseDb

data class HistoryDb(
    @Embedded val history: FlatHistoryDb,

    @Relation(
        entity = FlatReleaseDb::class,
        parentColumn = "releaseId",
        entityColumn = "releaseId"
    )
    val release: ReleaseDb?
)