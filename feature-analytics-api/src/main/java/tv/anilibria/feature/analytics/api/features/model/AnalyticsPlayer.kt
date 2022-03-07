package tv.anilibria.feature.analytics.api.features.model

enum class AnalyticsPlayer(val value: String) {
    NONE("none"),
    EXTERNAL("external"),
    INTERNAL("internal"),
    ALWAYS_ASK("always_ask"),
    UNKNOWN("unknown")
}