package tv.anilibria.module.data.analytics.features

import toothpick.InjectConstructor
import tv.anilibria.module.data.analytics.AnalyticsConstants
import tv.anilibria.module.data.analytics.features.extensions.toNavFromParam
import tv.anilibria.module.data.analytics.features.extensions.toPageParam
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class FavoritesAnalytics(
    private val sender: AnalyticsSender
) {

    fun open(from: String) {
        sender.send(
            AnalyticsConstants.favorites_open,
            from.toNavFromParam()
        )
    }

    fun searchClick() {
        sender.send(AnalyticsConstants.favorites_search_click)
    }

    fun searchReleaseClick() {
        sender.send(AnalyticsConstants.favorites_search_release_click)
    }

    fun releaseClick() {
        sender.send(AnalyticsConstants.favorites_release_click)
    }

    fun deleteFav() {
        sender.send(AnalyticsConstants.favorites_delete_click)
    }

    fun loadPage(page: Int) {
        sender.send(
            AnalyticsConstants.favorites_load_page,
            page.toPageParam()
        )
    }

}