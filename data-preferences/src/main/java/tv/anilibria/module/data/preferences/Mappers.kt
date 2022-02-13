package tv.anilibria.module.data.preferences

private const val QUALITY_NO = -1
private const val QUALITY_SD = 0
private const val QUALITY_HD = 1
private const val QUALITY_ALWAYS = 2
private const val QUALITY_FULL_HD = 3

private const val PLAYER_TYPE_NO = -1
private const val PLAYER_TYPE_EXTERNAL = 0
private const val PLAYER_TYPE_INTERNAL = 1
private const val PLAYER_TYPE_ALWAYS = 2

private const val PIP_BUTTON = 0
private const val PIP_AUTO = 1


fun Int.toPlayerQuality(): PlayerQuality = when (this) {
    QUALITY_NO -> PlayerQuality.NOT_SELECTED
    QUALITY_SD -> PlayerQuality.SD
    QUALITY_HD -> PlayerQuality.HD
    QUALITY_ALWAYS -> PlayerQuality.ALWAYS_ASK
    QUALITY_FULL_HD -> PlayerQuality.FULL_HD
    else -> PlayerQuality.NOT_SELECTED
}

fun Int.toPlayerType(): PlayerType = when (this) {
    PLAYER_TYPE_NO -> PlayerType.NOT_SELECTED
    PLAYER_TYPE_EXTERNAL -> PlayerType.EXTERNAL
    PLAYER_TYPE_INTERNAL -> PlayerType.INTERNAL
    PLAYER_TYPE_ALWAYS -> PlayerType.ALWAYS_ASK
    else -> PlayerType.NOT_SELECTED
}

fun Int.toPipMode(): PlayerPipMode = when (this) {
    PIP_BUTTON -> PlayerPipMode.BUTTON
    PIP_AUTO -> PlayerPipMode.AUTO
    else -> PlayerPipMode.AUTO
}

fun PlayerQuality.toPrefs(): Int = when (this) {
    PlayerQuality.NOT_SELECTED -> QUALITY_NO
    PlayerQuality.SD -> QUALITY_SD
    PlayerQuality.HD -> QUALITY_HD
    PlayerQuality.ALWAYS_ASK -> QUALITY_ALWAYS
    PlayerQuality.FULL_HD -> QUALITY_FULL_HD
}

fun PlayerType.toPrefs(): Int = when (this) {
    PlayerType.NOT_SELECTED -> PLAYER_TYPE_NO
    PlayerType.EXTERNAL -> PLAYER_TYPE_EXTERNAL
    PlayerType.INTERNAL -> PLAYER_TYPE_INTERNAL
    PlayerType.ALWAYS_ASK -> PLAYER_TYPE_ALWAYS
}

fun PlayerPipMode.toPrefs(): Int = when (this) {
    PlayerPipMode.BUTTON -> PIP_BUTTON
    PlayerPipMode.AUTO -> PIP_AUTO
}
