package tv.anilibria.feature.content.data.remote.entity.mapper

import kotlinx.datetime.DayOfWeek
import tv.anilibria.feature.domain.entity.other.DataColor
import tv.anilibria.feature.domain.entity.other.DataIcon

fun String.asWeekDay(): DayOfWeek {
    val intDay = requireNotNull(toIntOrNull()) {
        "Day is not integer '$this'"
    }
    return DayOfWeek(intDay)
}

fun String.toDataIcon(): DataIcon = when (this) {
    "vk" -> DataIcon.VK
    "youtube",
    "yotube" -> DataIcon.YOUTUBE
    "patreon" -> DataIcon.PATREON
    "telegram" -> DataIcon.TELEGRAM
    "discord" -> DataIcon.DISCORD
    "yoomoney" -> DataIcon.YOOMONEY
    "donationalerts" -> DataIcon.DONATIONALERTS
    "anilibria" -> DataIcon.ANILIBRIA
    "info" -> DataIcon.INFO
    "rules" -> DataIcon.RULES
    "person" -> DataIcon.PERSON
    "site" -> DataIcon.SITE
    "infra" -> DataIcon.INFRA
    else -> DataIcon.UNKNOWN
}

fun String.toDataColor(): DataColor = when (this) {
    "vk" -> DataColor.VK
    "youtube",
    "yotube" -> DataColor.YOUTUBE
    "patreon" -> DataColor.PATREON
    "telegram" -> DataColor.TELEGRAM
    "discord" -> DataColor.DISCORD
    "yoomoney" -> DataColor.YOOMONEY
    "donationalerts" -> DataColor.DONATIONALERTS
    "anilibria" -> DataColor.ANILIBRIA
    else -> DataColor.UNKNOWN
}