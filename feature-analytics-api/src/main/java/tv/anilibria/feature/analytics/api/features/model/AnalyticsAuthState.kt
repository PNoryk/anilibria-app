package tv.anilibria.feature.analytics.api.features.model

enum class AnalyticsAuthState(val value: String) {
    NO("no"),
    SKIP("skip"),
    AUTH("auth"),
    UNKNOWN("unknown")
}