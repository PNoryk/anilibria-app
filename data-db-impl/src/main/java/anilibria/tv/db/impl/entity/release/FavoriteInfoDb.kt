package anilibria.tv.db.impl.entity.release

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "release_favorite",
    foreignKeys = [
        ForeignKey(entity = FlatReleaseDb::class, parentColumns = ["releaseId"], childColumns = ["releaseId"])
    ]
)
data class FavoriteInfoDb(
    @PrimaryKey @ColumnInfo(name = "releaseId") val releaseId: Int,
    @ColumnInfo(name = "rating") val rating: Int,
    @ColumnInfo(name = "added") val added: Boolean
)