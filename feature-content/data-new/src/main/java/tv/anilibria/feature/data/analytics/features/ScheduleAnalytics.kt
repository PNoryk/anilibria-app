package tv.anilibria.feature.data.analytics.features

import toothpick.InjectConstructor
import tv.anilibria.feature.data.analytics.AnalyticsConstants
import tv.anilibria.feature.data.analytics.features.extensions.toNavFromParam
import tv.anilibria.feature.data.analytics.features.extensions.toPositionParam
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class ScheduleAnalytics(
    private val sender: AnalyticsSender
) {

    fun open(from: String) {
        sender.send(
            AnalyticsConstants.schedule_open,
            from.toNavFromParam()
        )
    }

    fun horizontalScroll(position: Int) {
        sender.send(
            AnalyticsConstants.schedule_horizontal_scroll,
            position.toPositionParam()
        )
    }

    fun releaseClick(position: Int) {
        sender.send(
            AnalyticsConstants.schedule_release_click,
            position.toPositionParam()
        )
    }
}