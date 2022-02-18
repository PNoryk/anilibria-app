package ru.radiationx.data.analytics.features.model

@Deprecated("old data")
enum class AnalyticsPlayer(val value: String) {
    NONE("none"),
    EXTERNAL("external"),
    INTERNAL("internal"),
    ALWAYS_ASK("always_ask"),
    UNKNOWN("unknown")
}