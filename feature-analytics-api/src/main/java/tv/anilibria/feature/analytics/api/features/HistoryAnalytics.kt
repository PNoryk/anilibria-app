package tv.anilibria.feature.analytics.api.features

import toothpick.InjectConstructor
import tv.anilibria.feature.analytics.api.AnalyticsConstants
import tv.anilibria.feature.analytics.api.features.extensions.toNavFromParam
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class HistoryAnalytics(
    private val sender: AnalyticsSender
) {

    fun open(from: String) {
        sender.send(
            AnalyticsConstants.history_open,
            from.toNavFromParam()
        )
    }

    fun searchClick() {
        sender.send(AnalyticsConstants.history_search_click)
    }

    fun searchReleaseClick() {
        sender.send(AnalyticsConstants.history_search_release_click)
    }

    fun releaseDeleteClick() {
        sender.send(AnalyticsConstants.history_release_delete_click)
    }

    fun releaseClick() {
        sender.send(AnalyticsConstants.history_release_click)
    }

}