package tv.anilibria.feature.analytics.features

import toothpick.InjectConstructor
import tv.anilibria.feature.analytics.AnalyticsConstants
import tv.anilibria.feature.analytics.features.extensions.toErrorParam
import tv.anilibria.feature.analytics.features.extensions.toIdParam
import tv.anilibria.feature.analytics.features.extensions.toNavFromParam
import tv.anilibria.feature.analytics.features.extensions.toTimeParam
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class WebPlayerAnalytics(
    private val sender: AnalyticsSender
) {

    fun open(from: String, releaseId: Long) {
        sender.send(
            AnalyticsConstants.web_player_open,
            from.toNavFromParam(),
            releaseId.toIdParam()
        )
    }

    fun loaded() {
        sender.send(AnalyticsConstants.web_player_loaded)
    }

    fun error(error: Throwable) {
        sender.send(
            AnalyticsConstants.web_player_error,
            error.toErrorParam()
        )
    }

    fun useTime(timeInMillis: Long) {
        sender.send(
            AnalyticsConstants.web_player_use_time,
            timeInMillis.toTimeParam()
        )
    }
}