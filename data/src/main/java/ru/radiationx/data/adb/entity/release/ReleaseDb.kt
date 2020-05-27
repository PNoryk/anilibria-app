package ru.radiationx.data.adb.entity.release

import androidx.room.Embedded
import androidx.room.Relation

data class ReleaseDb(
    @Embedded val release: FlatReleaseDb,

    @Relation(
        entity = FavoriteInfoDb::class,
        parentColumn = "releaseId",
        entityColumn = "releaseId"
    )
    val favorite: FavoriteInfoDb?,

    @Relation(
        entity = BlockInfoDb::class,
        parentColumn = "releaseId",
        entityColumn = "releaseId"
    )
    val blockedInfo: BlockInfoDb?
)