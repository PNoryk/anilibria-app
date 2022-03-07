package tv.anilibria.feature.data.analytics.features

import toothpick.InjectConstructor
import tv.anilibria.feature.data.analytics.AnalyticsConstants
import tv.anilibria.feature.data.analytics.features.extensions.toNavFromParam
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class FastSearchAnalytics(
    private val sender: AnalyticsSender
) {

    fun open(from: String) {
        sender.send(
            AnalyticsConstants.fast_search_open,
            from.toNavFromParam()
        )
    }

    fun releaseClick() {
        sender.send(AnalyticsConstants.fast_search_release_click)
    }

    fun catalogClick() {
        sender.send(AnalyticsConstants.fast_search_catalog_click)
    }

    fun searchGoogleClick() {
        sender.send(AnalyticsConstants.fast_search_google_click)
    }
}