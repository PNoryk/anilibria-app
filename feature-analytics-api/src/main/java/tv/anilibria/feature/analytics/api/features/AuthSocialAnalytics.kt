package tv.anilibria.feature.analytics.api.features

import toothpick.InjectConstructor
import tv.anilibria.feature.analytics.api.AnalyticsConstants
import tv.anilibria.feature.analytics.api.features.extensions.toErrorParam
import tv.anilibria.feature.analytics.api.features.extensions.toNavFromParam
import tv.anilibria.feature.analytics.api.features.extensions.toTimeParam
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class AuthSocialAnalytics(
    private val sender: AnalyticsSender
) {

    fun open(from: String) {
        sender.send(
            AnalyticsConstants.auth_social_open,
            from.toNavFromParam()
        )
    }

    fun error(error: Throwable) {
        sender.send(
            AnalyticsConstants.auth_social_error,
            error.toErrorParam()
        )
    }

    fun success() {
        sender.send(AnalyticsConstants.auth_social_success)
    }

    fun useTime(timeInMillis: Long) {
        sender.send(
            AnalyticsConstants.auth_social_use_time,
            timeInMillis.toTimeParam()
        )
    }

}