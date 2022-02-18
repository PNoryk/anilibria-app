package tv.anilibria.module.data.analytics.features.mapper

import tv.anilibria.module.data.analytics.features.model.*
import tv.anilibria.module.data.preferences.AppTheme
import tv.anilibria.module.data.preferences.PlayerPipMode
import tv.anilibria.module.data.preferences.PlayerQuality
import tv.anilibria.module.data.preferences.PlayerType
import tv.anilibria.module.domain.entity.AuthState

fun AppTheme.toAnalyticsAppTheme(): AnalyticsAppTheme = when (this) {
    AppTheme.LIGHT -> AnalyticsAppTheme.LIGHT
    AppTheme.DARK -> AnalyticsAppTheme.DARK
    else -> AnalyticsAppTheme.UNKNOWN
}

fun AuthState.toAnalyticsAuthState(): AnalyticsAuthState = when (this) {
    AuthState.NO_AUTH -> AnalyticsAuthState.NO
    AuthState.AUTH_SKIPPED -> AnalyticsAuthState.SKIP
    AuthState.AUTH -> AnalyticsAuthState.AUTH
    else -> AnalyticsAuthState.UNKNOWN
}

fun PlayerQuality.toAnalyticsQuality(): AnalyticsQuality = when (this) {
    PlayerQuality.SD -> AnalyticsQuality.SD
    PlayerQuality.HD -> AnalyticsQuality.HD
    PlayerQuality.FULL_HD -> AnalyticsQuality.FULL_HD
    PlayerQuality.NOT_SELECTED -> AnalyticsQuality.NONE
    PlayerQuality.ALWAYS_ASK -> AnalyticsQuality.ALWAYS_ASK
    else -> AnalyticsQuality.UNKNOWN
}

fun PlayerType.toAnalyticsPlayer(): AnalyticsPlayer = when (this) {
    PlayerType.EXTERNAL -> AnalyticsPlayer.EXTERNAL
    PlayerType.INTERNAL -> AnalyticsPlayer.INTERNAL
    PlayerType.NOT_SELECTED -> AnalyticsPlayer.NONE
    PlayerType.ALWAYS_ASK -> AnalyticsPlayer.ALWAYS_ASK
    else -> AnalyticsPlayer.UNKNOWN
}

fun PlayerPipMode.toAnalyticsPip(): AnalyticsPip = when (this) {
    PlayerPipMode.AUTO -> AnalyticsPip.AUTO
    PlayerPipMode.BUTTON -> AnalyticsPip.BUTTON
    else -> AnalyticsPip.UNKNOWN
}

fun Int.toAnalyticsScale(): AnalyticsVideoScale = when (this) {
    0 -> AnalyticsVideoScale.CENTER
    1 -> AnalyticsVideoScale.CENTER_CROP
    2 -> AnalyticsVideoScale.CENTER_INSIDE
    3 -> AnalyticsVideoScale.FIT_CENTER
    4 -> AnalyticsVideoScale.FIT_XY
    5 -> AnalyticsVideoScale.NONE
    else -> AnalyticsVideoScale.UNKNOWN
}
