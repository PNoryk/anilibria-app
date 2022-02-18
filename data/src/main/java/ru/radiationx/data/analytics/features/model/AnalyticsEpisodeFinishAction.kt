package ru.radiationx.data.analytics.features.model

@Deprecated("old data")
enum class AnalyticsEpisodeFinishAction(val value: String) {
    RESTART("restart_episode"),
    NEXT("next")
}