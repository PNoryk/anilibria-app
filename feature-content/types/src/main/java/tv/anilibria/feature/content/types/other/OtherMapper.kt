package tv.anilibria.feature.content.types.other


private const val key_vk = "vk"
private const val key_youtube = "youtube"
private const val key_youtube_bruh = "yotube"
private const val key_patreon = "patreon"
private const val key_telegram = "telegram"
private const val key_discord = "discord"
private const val key_yoomoney = "yoomoney"
private const val key_donationalerts = "donationalerts"
private const val key_anilibria = "anilibria"
private const val key_info = "info"
private const val key_rules = "rules"
private const val key_person = "person"
private const val key_site = "site"
private const val key_infra = "infra"
private const val key_unknown = "unknown"


fun DataIcon.toKey(): String = when (this) {
    DataIcon.VK -> key_vk
    DataIcon.YOUTUBE -> key_youtube
    DataIcon.PATREON -> key_patreon
    DataIcon.TELEGRAM -> key_telegram
    DataIcon.DISCORD -> key_discord
    DataIcon.YOOMONEY -> key_yoomoney
    DataIcon.DONATIONALERTS -> key_donationalerts
    DataIcon.ANILIBRIA -> key_anilibria
    DataIcon.INFO -> key_info
    DataIcon.RULES -> key_rules
    DataIcon.PERSON -> key_person
    DataIcon.SITE -> key_site
    DataIcon.INFRA -> key_infra
    DataIcon.UNKNOWN -> key_unknown
}

fun DataColor.toKey(): String = when (this) {
    DataColor.VK -> key_vk
    DataColor.YOUTUBE -> key_youtube
    DataColor.PATREON -> key_patreon
    DataColor.TELEGRAM -> key_telegram
    DataColor.DISCORD -> key_discord
    DataColor.YOOMONEY -> key_yoomoney
    DataColor.DONATIONALERTS -> key_donationalerts
    DataColor.ANILIBRIA -> key_anilibria
    DataColor.UNKNOWN -> key_unknown
}

fun String.toDataIcon(): DataIcon = when (this) {
    key_vk -> DataIcon.VK
    key_youtube_bruh,
    key_youtube -> DataIcon.YOUTUBE
    key_patreon -> DataIcon.PATREON
    key_telegram -> DataIcon.TELEGRAM
    key_discord -> DataIcon.DISCORD
    key_yoomoney -> DataIcon.YOOMONEY
    key_donationalerts -> DataIcon.DONATIONALERTS
    key_anilibria -> DataIcon.ANILIBRIA
    key_info -> DataIcon.INFO
    key_rules -> DataIcon.RULES
    key_person -> DataIcon.PERSON
    key_site -> DataIcon.SITE
    key_infra -> DataIcon.INFRA
    else -> DataIcon.UNKNOWN
}

fun String.toDataColor(): DataColor = when (this) {
    key_vk -> DataColor.VK
    key_youtube_bruh,
    key_youtube -> DataColor.YOUTUBE
    key_patreon -> DataColor.PATREON
    key_telegram -> DataColor.TELEGRAM
    key_discord -> DataColor.DISCORD
    key_yoomoney -> DataColor.YOOMONEY
    key_donationalerts -> DataColor.DONATIONALERTS
    key_anilibria -> DataColor.ANILIBRIA
    else -> DataColor.UNKNOWN
}