package tv.anilibria.feature.player.data.prefs

object PrefsConstants {
    const val QUALITY_NO = -1
    const val QUALITY_SD = 0
    const val QUALITY_HD = 1
    const val QUALITY_ALWAYS = 2
    const val QUALITY_FULL_HD = 3

    const val PLAYER_TYPE_NO = -1
    const val PLAYER_TYPE_EXTERNAL = 0
    const val PLAYER_TYPE_INTERNAL = 1
    const val PLAYER_TYPE_ALWAYS = 2

    const val PIP_BUTTON = 0
    const val PIP_AUTO = 1

    const val DEFAULT_PLAYER_SPEED = 1.0f
}


fun Int.toPlayerQuality(): PrefferedPlayerQuality = when (this) {
    PrefsConstants.QUALITY_NO -> PrefferedPlayerQuality.NOT_SELECTED
    PrefsConstants.QUALITY_SD -> PrefferedPlayerQuality.SD
    PrefsConstants.QUALITY_HD -> PrefferedPlayerQuality.HD
    PrefsConstants.QUALITY_ALWAYS -> PrefferedPlayerQuality.ALWAYS_ASK
    PrefsConstants.QUALITY_FULL_HD -> PrefferedPlayerQuality.FULL_HD
    else -> PrefferedPlayerQuality.NOT_SELECTED
}

fun Int.toPlayerType(): PrefferedPlayerType = when (this) {
    PrefsConstants.PLAYER_TYPE_NO -> PrefferedPlayerType.NOT_SELECTED
    PrefsConstants.PLAYER_TYPE_EXTERNAL -> PrefferedPlayerType.EXTERNAL
    PrefsConstants.PLAYER_TYPE_INTERNAL -> PrefferedPlayerType.INTERNAL
    PrefsConstants.PLAYER_TYPE_ALWAYS -> PrefferedPlayerType.ALWAYS_ASK
    else -> PrefferedPlayerType.NOT_SELECTED
}

fun Int.toPipMode(): PrefferedPlayerPipMode = when (this) {
    PrefsConstants.PIP_BUTTON -> PrefferedPlayerPipMode.BUTTON
    PrefsConstants.PIP_AUTO -> PrefferedPlayerPipMode.AUTO
    else -> PrefferedPlayerPipMode.AUTO
}

fun PrefferedPlayerQuality.toPrefs(): Int = when (this) {
    PrefferedPlayerQuality.NOT_SELECTED -> PrefsConstants.QUALITY_NO
    PrefferedPlayerQuality.SD -> PrefsConstants.QUALITY_SD
    PrefferedPlayerQuality.HD -> PrefsConstants.QUALITY_HD
    PrefferedPlayerQuality.ALWAYS_ASK -> PrefsConstants.QUALITY_ALWAYS
    PrefferedPlayerQuality.FULL_HD -> PrefsConstants.QUALITY_FULL_HD
}

fun PrefferedPlayerType.toPrefs(): Int = when (this) {
    PrefferedPlayerType.NOT_SELECTED -> PrefsConstants.PLAYER_TYPE_NO
    PrefferedPlayerType.EXTERNAL -> PrefsConstants.PLAYER_TYPE_EXTERNAL
    PrefferedPlayerType.INTERNAL -> PrefsConstants.PLAYER_TYPE_INTERNAL
    PrefferedPlayerType.ALWAYS_ASK -> PrefsConstants.PLAYER_TYPE_ALWAYS
}

fun PrefferedPlayerPipMode.toPrefs(): Int = when (this) {
    PrefferedPlayerPipMode.BUTTON -> PrefsConstants.PIP_BUTTON
    PrefferedPlayerPipMode.AUTO -> PrefsConstants.PIP_AUTO
}
