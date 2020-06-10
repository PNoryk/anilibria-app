package anilibria.tv.db.impl.entity.release

import androidx.room.Embedded
import androidx.room.Relation

data class ReleaseDb(
    @Embedded val release: FlatReleaseDb,

    @Relation(
        entity = FavoriteInfoDb::class,
        parentColumn = "id",
        entityColumn = "releaseId"
    )
    val favorite: FavoriteInfoDb?,

    @Relation(
        entity = BlockInfoDb::class,
        parentColumn = "id",
        entityColumn = "releaseId"
    )
    val blockedInfo: BlockInfoDb?
)