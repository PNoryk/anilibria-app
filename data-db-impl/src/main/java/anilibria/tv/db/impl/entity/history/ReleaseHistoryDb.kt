package anilibria.tv.db.impl.entity.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import anilibria.tv.db.impl.entity.release.FlatReleaseDb
import java.util.*

@Entity(
    tableName = "release_history",
    foreignKeys = [
        ForeignKey(entity = FlatReleaseDb::class, parentColumns = ["releaseId"], childColumns = ["releaseId"])
    ]
)
data class ReleaseHistoryDb(
    @PrimaryKey @ColumnInfo(name = "releaseId") val releaseId: Int,
    @ColumnInfo(name = "timestamp") val timestamp: Date
)