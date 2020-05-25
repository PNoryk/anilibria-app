package ru.radiationx.data.adb.release

import androidx.room.Embedded
import androidx.room.Relation
import ru.radiationx.data.adb.release.BlockInfoDb
import ru.radiationx.data.adb.release.FavoriteInfoDb
import ru.radiationx.data.adb.release.FlatReleaseDb

data class ReleaseDb(
    @Embedded val release: FlatReleaseDb,

    @Relation(
        parentColumn = "releaseId",
        entityColumn = "releaseId"
    )
    val favorite: FavoriteInfoDb?,

    @Relation(
        parentColumn = "releaseId",
        entityColumn = "releaseId"
    )
    val blockedInfo: BlockInfoDb?
)