package anilibria.tv.db.impl.entity.favorite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import anilibria.tv.db.impl.entity.release.FlatReleaseDb

@Entity(
    tableName = "favorite",
    foreignKeys = [
        ForeignKey(entity = FlatReleaseDb::class, parentColumns = ["id"], childColumns = ["releaseId"])
    ]
)
data class FavoriteDb(
    @PrimaryKey @ColumnInfo(name = "releaseId") val releaseId: Int
)