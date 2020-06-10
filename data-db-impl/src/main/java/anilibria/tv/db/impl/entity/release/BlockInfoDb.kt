package anilibria.tv.db.impl.entity.release

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "release_block",
    foreignKeys = [
        ForeignKey(entity = FlatReleaseDb::class, parentColumns = ["id"], childColumns = ["releaseId"])
    ]
)
data class BlockInfoDb(
    @PrimaryKey @ColumnInfo(name = "releaseId") val releaseId: Int,
    @ColumnInfo(name = "blocked") val blocked: Boolean,
    @ColumnInfo(name = "reason") val reason: String?
)