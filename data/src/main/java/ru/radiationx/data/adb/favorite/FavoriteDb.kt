package ru.radiationx.data.adb.favorite

import androidx.room.Embedded
import androidx.room.Relation
import ru.radiationx.data.adb.feed.FlatFeedDb
import ru.radiationx.data.adb.release.FlatReleaseDb
import ru.radiationx.data.adb.release.ReleaseDb

data class FavoriteDb(
    @Embedded val favorite: FlatFavoriteDb,

    @Relation(
        entity = FlatReleaseDb::class,
        parentColumn = "releaseId",
        entityColumn = "releaseId"
    )
    val release: ReleaseDb
)