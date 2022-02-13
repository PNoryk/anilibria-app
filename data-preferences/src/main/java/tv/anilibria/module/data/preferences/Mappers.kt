package tv.anilibria.module.data.preferences

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


fun Int.toPlayerQuality(): PlayerQuality = when (this) {
    PrefsConstants.QUALITY_NO -> PlayerQuality.NOT_SELECTED
    PrefsConstants.QUALITY_SD -> PlayerQuality.SD
    PrefsConstants.QUALITY_HD -> PlayerQuality.HD
    PrefsConstants.QUALITY_ALWAYS -> PlayerQuality.ALWAYS_ASK
    PrefsConstants.QUALITY_FULL_HD -> PlayerQuality.FULL_HD
    else -> PlayerQuality.NOT_SELECTED
}

fun Int.toPlayerType(): PlayerType = when (this) {
    PrefsConstants.PLAYER_TYPE_NO -> PlayerType.NOT_SELECTED
    PrefsConstants.PLAYER_TYPE_EXTERNAL -> PlayerType.EXTERNAL
    PrefsConstants.PLAYER_TYPE_INTERNAL -> PlayerType.INTERNAL
    PrefsConstants.PLAYER_TYPE_ALWAYS -> PlayerType.ALWAYS_ASK
    else -> PlayerType.NOT_SELECTED
}

fun Int.toPipMode(): PlayerPipMode = when (this) {
    PrefsConstants.PIP_BUTTON -> PlayerPipMode.BUTTON
    PrefsConstants.PIP_AUTO -> PlayerPipMode.AUTO
    else -> PlayerPipMode.AUTO
}

fun PlayerQuality.toPrefs(): Int = when (this) {
    PlayerQuality.NOT_SELECTED -> PrefsConstants.QUALITY_NO
    PlayerQuality.SD -> PrefsConstants.QUALITY_SD
    PlayerQuality.HD -> PrefsConstants.QUALITY_HD
    PlayerQuality.ALWAYS_ASK -> PrefsConstants.QUALITY_ALWAYS
    PlayerQuality.FULL_HD -> PrefsConstants.QUALITY_FULL_HD
}

fun PlayerType.toPrefs(): Int = when (this) {
    PlayerType.NOT_SELECTED -> PrefsConstants.PLAYER_TYPE_NO
    PlayerType.EXTERNAL -> PrefsConstants.PLAYER_TYPE_EXTERNAL
    PlayerType.INTERNAL -> PrefsConstants.PLAYER_TYPE_INTERNAL
    PlayerType.ALWAYS_ASK -> PrefsConstants.PLAYER_TYPE_ALWAYS
}

fun PlayerPipMode.toPrefs(): Int = when (this) {
    PlayerPipMode.BUTTON -> PrefsConstants.PIP_BUTTON
    PlayerPipMode.AUTO -> PrefsConstants.PIP_AUTO
}
