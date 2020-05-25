package ru.radiationx.data.adb.favorite

import androidx.room.Embedded
import androidx.room.Relation
import ru.radiationx.data.adb.feed.FlatFeedDb
import ru.radiationx.data.adb.release.ReleaseDb

class FavoriteDb(
    @Embedded val favorite: FlatFavoriteDb,

    @Relation(
        parentColumn = "releaseId",
        entityColumn = "releaseId"
    )
    val release: ReleaseDb?
)