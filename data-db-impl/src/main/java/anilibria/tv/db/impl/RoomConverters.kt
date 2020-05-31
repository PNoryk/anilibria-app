package anilibria.tv.db.impl

import androidx.room.TypeConverter
import anilibria.tv.domain.entity.release.Release
import java.util.*

class RoomConverters {

    @TypeConverter
    fun stringListToString(value: List<String>?): String? = value?.joinToString(",")

    @TypeConverter
    fun stringToStringList(value: String?): List<String>? = value?.split(",")


    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time


    @TypeConverter
    fun releaseStatusToString(value: Release.Status?): String? = when (value) {
        Release.Status.PROGRESS -> "1"
        Release.Status.COMPLETE -> "2"
        Release.Status.HIDDEN -> "3"
        Release.Status.ONGOING -> "4"
        else -> null
    }

    @TypeConverter
    fun stringToReleaseStatus(value: String?): Release.Status? = when (value) {
        "1" -> Release.Status.PROGRESS
        "2" -> Release.Status.COMPLETE
        "3" -> Release.Status.HIDDEN
        "4" -> Release.Status.ONGOING
        else -> null
    }
}