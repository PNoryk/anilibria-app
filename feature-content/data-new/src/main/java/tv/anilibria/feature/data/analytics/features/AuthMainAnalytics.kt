package tv.anilibria.feature.data.analytics.features

import toothpick.InjectConstructor
import tv.anilibria.feature.data.analytics.AnalyticsConstants
import tv.anilibria.feature.data.analytics.features.extensions.toErrorParam
import tv.anilibria.feature.data.analytics.features.extensions.toNavFromParam
import tv.anilibria.feature.data.analytics.features.extensions.toParam
import tv.anilibria.feature.data.analytics.features.extensions.toTimeParam
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class AuthMainAnalytics(
    private val sender: AnalyticsSender
) {

    private companion object {
        const val PARAM_KEY = "key"
    }

    fun open(from: String) {
        sender.send(
            AnalyticsConstants.auth_main_open,
            from.toNavFromParam()
        )
    }

    fun socialClick(key: String) {
        sender.send(
            AnalyticsConstants.auth_main_social_click,
            key.toParam(PARAM_KEY)
        )
    }

    fun regClick() {
        sender.send(AnalyticsConstants.auth_main_reg_click)
    }

    fun regToSiteClick() {
        sender.send(AnalyticsConstants.auth_main_reg_to_site_click)
    }

    fun skipClick() {
        sender.send(AnalyticsConstants.auth_main_skip_click)
    }

    fun loginClick() {
        sender.send(AnalyticsConstants.auth_main_login_click)
    }

    fun error(error: Throwable) {
        sender.send(
            AnalyticsConstants.auth_main_error,
            error.toErrorParam()
        )
    }

    fun success() {
        sender.send(AnalyticsConstants.auth_main_success)
    }

    fun wrongSuccess() {
        sender.send(AnalyticsConstants.auth_main_wrong_success)
    }

    fun useTime(timeInMillis: Long) {
        sender.send(
            AnalyticsConstants.auth_main_use_time,
            timeInMillis.toTimeParam()
        )
    }

}