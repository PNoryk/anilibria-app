package tv.anilibria.feature.content.data.remote.entity.mapper

import kotlinx.datetime.DayOfWeek
import tv.anilibria.feature.content.types.other.DataColor
import tv.anilibria.feature.content.types.other.DataIcon

fun String.asWeekDay(): DayOfWeek {
    val intDay = requireNotNull(toIntOrNull()) {
        "Day is not integer '$this'"
    }
    return DayOfWeek(intDay)
}