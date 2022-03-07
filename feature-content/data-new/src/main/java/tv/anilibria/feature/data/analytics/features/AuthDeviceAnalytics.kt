package tv.anilibria.feature.data.analytics.features

import toothpick.InjectConstructor
import tv.anilibria.feature.data.analytics.AnalyticsConstants
import tv.anilibria.feature.data.analytics.features.extensions.toErrorParam
import tv.anilibria.feature.data.analytics.features.extensions.toNavFromParam
import tv.anilibria.feature.data.analytics.features.extensions.toTimeParam
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class AuthDeviceAnalytics(
    private val sender: AnalyticsSender
) {

    fun open(from: String) {
        sender.send(
            AnalyticsConstants.auth_device_open,
            from.toNavFromParam()
        )
    }

    fun error(error: Throwable) {
        sender.send(
            AnalyticsConstants.auth_device_error,
            error.toErrorParam()
        )
    }

    fun success() {
        sender.send(AnalyticsConstants.auth_device_success)
    }

    fun useTime(timeInMillis: Long) {
        sender.send(
            AnalyticsConstants.auth_device_use_time,
            timeInMillis.toTimeParam()
        )
    }

}