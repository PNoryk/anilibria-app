package tv.anilibria.module.data.analytics.features

import toothpick.InjectConstructor
import tv.anilibria.module.data.analytics.AnalyticsConstants
import tv.anilibria.module.data.analytics.features.extensions.toPreciseTimeParam
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class AppAnalytics(
    private val sender: AnalyticsSender
) {

    fun timeToCreate(timeInMillis: Long) {
        sender.send(
            AnalyticsConstants.app_time_to_create,
            timeInMillis.toPreciseTimeParam()
        )
    }

    fun timeToInit(timeInMillis: Long) {
        sender.send(
            AnalyticsConstants.app_time_to_init,
            timeInMillis.toPreciseTimeParam()
        )
    }

    fun timeToActivity(timeInMillis: Long) {
        sender.send(
            AnalyticsConstants.app_time_to_activity,
            timeInMillis.toPreciseTimeParam()
        )
    }
}