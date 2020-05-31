package anilibria.tv.db.impl.entity.release

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import anilibria.tv.domain.entity.release.Release
import java.util.*

@Entity(tableName = "release")
data class FlatReleaseDb(
    @PrimaryKey @ColumnInfo(name = "releaseId") val id: Int,
    @ColumnInfo(name = "code") val code: String?,
    @ColumnInfo(name = "nameRu") val nameRu: String?,
    @ColumnInfo(name = "nameEn") val nameEn: String?,
    @ColumnInfo(name = "series") val series: String?,
    @ColumnInfo(name = "poster") val poster: String?,
    @ColumnInfo(name = "last") val last: Date?,
    @ColumnInfo(name = "webPlayer") val webPlayer: String?,
    @ColumnInfo(name = "status") val status: Release.Status?,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "genres") val genres: List<String>?,
    @ColumnInfo(name = "voices") val voices: List<String>?,
    @ColumnInfo(name = "year") val year: String?,
    @ColumnInfo(name = "day") val day: Int?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "announce") val announce: String?,
    @ColumnInfo(name = "showDonateDialog") val showDonateDialog: Boolean?
)