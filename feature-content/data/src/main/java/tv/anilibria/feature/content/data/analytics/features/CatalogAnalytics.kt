package tv.anilibria.feature.content.data.analytics.features

import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.analytics.AnalyticsConstants
import tv.anilibria.feature.content.data.analytics.features.extensions.toNavFromParam
import tv.anilibria.feature.content.data.analytics.features.extensions.toPageParam
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class CatalogAnalytics(
    private val sender: AnalyticsSender
) {

    fun open(from: String) {
        sender.send(
            AnalyticsConstants.catalog_open,
            from.toNavFromParam()
        )
    }

    fun releaseClick() {
        sender.send(AnalyticsConstants.catalog_release_click)
    }

    fun fastSearchClick() {
        sender.send(AnalyticsConstants.catalog_fast_search_click)
    }

    fun filterClick() {
        sender.send(AnalyticsConstants.catalog_on_filter_click)
    }

    fun loadPage(page: Int) {
        sender.send(
            AnalyticsConstants.catalog_load_page,
            page.toPageParam()
        )
    }

}