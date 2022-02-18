package ru.radiationx.data.analytics.features.model

@Deprecated("old data")
enum class AnalyticsAuthState(val value:String) {
    NO("no"),
    SKIP("skip"),
    AUTH("auth"),
    UNKNOWN("unknown")
}