package ru.radiationx.data.adb.entity.favorite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.radiationx.data.adb.entity.release.FlatReleaseDb

@Entity(
    tableName = "favorite",
    foreignKeys = [
        ForeignKey(entity = FlatReleaseDb::class, parentColumns = ["releaseId"], childColumns = ["releaseId"])
    ]
)
data class FavoriteDb(
    @PrimaryKey @ColumnInfo(name = "releaseId") val releaseId: Int
)