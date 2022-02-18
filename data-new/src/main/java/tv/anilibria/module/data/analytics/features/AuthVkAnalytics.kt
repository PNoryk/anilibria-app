package tv.anilibria.module.data.analytics.features

import toothpick.InjectConstructor
import tv.anilibria.module.data.analytics.AnalyticsConstants
import tv.anilibria.module.data.analytics.features.extensions.toErrorParam
import tv.anilibria.module.data.analytics.features.extensions.toNavFromParam
import tv.anilibria.module.data.analytics.features.extensions.toTimeParam
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class AuthVkAnalytics(
    private val sender: AnalyticsSender
) {

    fun open(from: String) {
        sender.send(
            AnalyticsConstants.auth_vk_open,
            from.toNavFromParam()
        )
    }

    fun error(error: Throwable) {
        sender.send(
            AnalyticsConstants.auth_vk_error,
            error.toErrorParam()
        )
    }

    fun success() {
        sender.send(AnalyticsConstants.auth_vk_success)
    }

    fun useTime(timeInMillis: Long) {
        sender.send(
            AnalyticsConstants.auth_vk_use_time,
            timeInMillis.toTimeParam()
        )
    }

}