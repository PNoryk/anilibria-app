package tv.anilibria.module.data.analytics.features.mapper

import tv.anilibria.module.data.analytics.features.model.*
import tv.anilibria.module.data.preferences.AppTheme
import tv.anilibria.module.data.preferences.PrefferedPlayerPipMode
import tv.anilibria.module.data.preferences.PrefferedPlayerQuality
import tv.anilibria.module.data.preferences.PrefferedPlayerType
import tv.anilibria.feature.auth.data.domain.AuthState

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

fun PrefferedPlayerQuality.toAnalyticsQuality(): AnalyticsQuality = when (this) {
    PrefferedPlayerQuality.SD -> AnalyticsQuality.SD
    PrefferedPlayerQuality.HD -> AnalyticsQuality.HD
    PrefferedPlayerQuality.FULL_HD -> AnalyticsQuality.FULL_HD
    PrefferedPlayerQuality.NOT_SELECTED -> AnalyticsQuality.NONE
    PrefferedPlayerQuality.ALWAYS_ASK -> AnalyticsQuality.ALWAYS_ASK
    else -> AnalyticsQuality.UNKNOWN
}

fun PrefferedPlayerType.toAnalyticsPlayer(): AnalyticsPlayer = when (this) {
    PrefferedPlayerType.EXTERNAL -> AnalyticsPlayer.EXTERNAL
    PrefferedPlayerType.INTERNAL -> AnalyticsPlayer.INTERNAL
    PrefferedPlayerType.NOT_SELECTED -> AnalyticsPlayer.NONE
    PrefferedPlayerType.ALWAYS_ASK -> AnalyticsPlayer.ALWAYS_ASK
    else -> AnalyticsPlayer.UNKNOWN
}

fun PrefferedPlayerPipMode.toAnalyticsPip(): AnalyticsPip = when (this) {
    PrefferedPlayerPipMode.AUTO -> AnalyticsPip.AUTO
    PrefferedPlayerPipMode.BUTTON -> AnalyticsPip.BUTTON
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
