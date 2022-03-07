package tv.anilibria.feature.analytics.api.features

import toothpick.InjectConstructor
import tv.anilibria.feature.analytics.api.AnalyticsConstants
import tv.anilibria.feature.analytics.api.features.extensions.toNavFromParam
import tv.anilibria.feature.analytics.api.features.extensions.toTimeParam
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class CatalogFilterAnalytics(
    private val sender: AnalyticsSender
) {

    fun open(from: String) {
        sender.send(
            AnalyticsConstants.catalog_filter_open,
            from.toNavFromParam()
        )
    }

    fun useTime(timeInMillis: Long) {
        sender.send(
            AnalyticsConstants.catalog_filter_use_time,
            timeInMillis.toTimeParam()
        )
    }

    fun applyClick() {
        sender.send(AnalyticsConstants.catalog_filter_apply_click)
    }

}