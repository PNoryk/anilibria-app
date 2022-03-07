package tv.anilibria.feature.content.data.local.mappers

import tv.anilibria.feature.content.types.other.DataColor
import tv.anilibria.feature.content.types.other.DataIcon

fun DataIcon.toLocal(): String = when (this) {
    DataIcon.VK -> "vk"
    DataIcon.YOUTUBE -> "youtube"
    DataIcon.PATREON -> "patreon"
    DataIcon.TELEGRAM -> "telegram"
    DataIcon.DISCORD -> "discord"
    DataIcon.YOOMONEY -> "yoomoney"
    DataIcon.DONATIONALERTS -> "donationalerts"
    DataIcon.ANILIBRIA -> "anilibria"
    DataIcon.INFO -> "info"
    DataIcon.RULES -> "rules"
    DataIcon.PERSON -> "person"
    DataIcon.SITE -> "site"
    DataIcon.INFRA -> "infra"
    DataIcon.UNKNOWN -> "unknown"
}

fun DataColor.toLocal(): String = when (this) {
    DataColor.VK -> "vk"
    DataColor.YOUTUBE -> "youtube"
    DataColor.PATREON -> "patreon"
    DataColor.TELEGRAM -> "telegram"
    DataColor.DISCORD -> "discord"
    DataColor.YOOMONEY -> "yoomoney"
    DataColor.DONATIONALERTS -> "donationalerts"
    DataColor.ANILIBRIA -> "anilibria"
    DataColor.UNKNOWN -> "unknown"
}

fun String.toDataIcon(): DataIcon = when (this) {
    "vk" -> DataIcon.VK
    "youtube" -> DataIcon.YOUTUBE
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
    "youtube" -> DataColor.YOUTUBE
    "patreon" -> DataColor.PATREON
    "telegram" -> DataColor.TELEGRAM
    "discord" -> DataColor.DISCORD
    "yoomoney" -> DataColor.YOOMONEY
    "donationalerts" -> DataColor.DONATIONALERTS
    "anilibria" -> DataColor.ANILIBRIA
    else -> DataColor.UNKNOWN
}