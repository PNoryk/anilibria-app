package tv.anilibria.module.data.analytics.features

import toothpick.InjectConstructor
import tv.anilibria.module.data.analytics.AnalyticsConstants
import tv.anilibria.module.data.analytics.features.extensions.toNavFromParam
import tv.anilibria.plugin.data.analytics.AnalyticsSender

@InjectConstructor
class Analytics(
    private val sender: AnalyticsSender
) {

    fun open(from: String) {
        sender.send(
            AnalyticsConstants.catalog_open,
            from.toNavFromParam()
        )
    }

}