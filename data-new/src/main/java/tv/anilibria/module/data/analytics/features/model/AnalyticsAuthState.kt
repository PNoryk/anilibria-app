package tv.anilibria.module.data.analytics.features.model

enum class AnalyticsAuthState(val value: String) {
    NO("no"),
    SKIP("skip"),
    AUTH("auth"),
    UNKNOWN("unknown")
}